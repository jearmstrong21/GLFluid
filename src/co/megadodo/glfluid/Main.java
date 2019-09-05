package co.megadodo.glfluid;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL41.*;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    public static Mesh quad;
    public static int SIMW = 1024/2;
    public static int SIMH = 1024/2;
    public static int WINW = SIMW*4;
    public static int WINH = SIMH*4;
    public static float RHO = 0.1F;
    public static float DT = 0.1F;
    public static float EPSILON = 1.0F / SIMW;

    public static void main(String[] __) {
        GLFWErrorCallback.createPrint(System.err).set();

        glfwInit();

        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER,GLFW_FALSE);

        long window = glfwCreateWindow(WINW / 2, WINH / 2, "GL Fluid", 0, 0);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();

        System.out.println("LWJGL VERSION: " + Version.getVersion());
        System.out.println("OPENGL VERSION: " + glGetInteger(GL_MAJOR_VERSION) + "." + glGetInteger(GL_MINOR_VERSION));

        quad = new Mesh();
        quad.addBuffer2f(0, new float[]{
                -1, -1,
                -1, 1,
                1, -1,
                1, 1
        });
        quad.addBuffer2f(1, new float[]{
                0, 0,
                0, SIMH,
                SIMW, 0,
                SIMW, SIMH
        });
        quad.setIndices(new int[]{
                0, 1, 3,
                0, 2, 3
        });

        GridData density = new GridData();
        GridData velocity = new GridData();

        GridData temp = new GridData();
        GridData divergence = new GridData();
        GridData pressure = new GridData();

        Computation display = new Computation("display");
        Computation initDensity = new Computation("init_density");
        Computation initVelocity = new Computation("init_velocity");
        Computation advect = new Computation("advect");
        Computation copy = new Computation("copy");
        Computation calcDivergence = new Computation("divergence");
        Computation pressureIteration = new Computation("pressure_iteration");
        Computation allZero = new Computation("all_zero");
        Computation fixPressure = new Computation("fix_pressure");
        Computation velocityMouse = new Computation("velocity_mouse");
        Computation clampAboveZero = new Computation("clamp_above_zero");
        Computation densityMouse=new Computation("density_mouse");

        Computation blur=new Computation("postprocess/blur");
        Computation threshold=new Computation("postprocess/threshold");
        Computation recombine=new Computation("postprocess/recombine");

        GridData finalDisplay=new GridData();
        GridData originalDisplay=new GridData();

        glClearColor(1, 1, 1, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        initDensity.run(new ShaderArguments(), density);
        initVelocity.run(new ShaderArguments(), velocity);

        float lmx=0,lmy=0;

        while (!glfwWindowShouldClose(window)) {
            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float mouseX, mouseY;
            {
                double[] mx = new double[1], my = new double[1];
                glfwGetCursorPos(window, mx, my);
                mouseX = (float) mx[0];
                mouseY = (float) my[0];
            }
            mouseX*=(float)SIMW/WINW;
            mouseY*=(float)SIMH/WINH;
            mouseX*=2;
            mouseY*=2;
            mouseY=SIMH-mouseY;
//            System.out.println(mouseX+" "+mouseY);
            if(glfwGetKey(window,GLFW_KEY_1)==GLFW_PRESS){
                velocityMouse.run(new ShaderArguments().put("velocity", velocity).put("mousePos", mouseX, mouseY).put("mouseDir", mouseX-lmx,mouseY-lmy), temp);
                copy.run(new ShaderArguments().put("source", temp), velocity);
            }
            if(glfwGetKey(window,GLFW_KEY_2)==GLFW_PRESS){
                densityMouse.run(new ShaderArguments().put("density",density).put("mousePos",mouseX,mouseY).put("change",
                        glfwGetKey(window,GLFW_KEY_3)==GLFW_PRESS?1.0F:-1.0F
                        ),temp);
                copy.run(new ShaderArguments().put("source",temp),density);
            }

            clampAboveZero.run(new ShaderArguments().put("source", density), temp);
            copy.run(new ShaderArguments().put("source", temp), density);



            advect.run(new ShaderArguments().put("source", density).put("velocity", velocity), temp);
            copy.run(new ShaderArguments().put("source", temp), density);

            advect.run(new ShaderArguments().put("source", velocity).put("velocity", velocity), temp);
            copy.run(new ShaderArguments().put("source", temp), velocity);

            calcDivergence.run(new ShaderArguments().put("velocity", velocity), divergence);

            allZero.run(new ShaderArguments(), pressure);
            for (int i = 0; i < 50; i++) {
                pressureIteration.run(new ShaderArguments().put("divergence", divergence).put("last_pressure", pressure), temp);
                copy.run(new ShaderArguments().put("source", temp), pressure);
            }

            fixPressure.run(new ShaderArguments().put("pressure", pressure).put("velocity", velocity), temp);
            copy.run(new ShaderArguments().put("source", temp), velocity);


            {
                display.run(new ShaderArguments().put("density", density),originalDisplay);
                copy.run(new ShaderArguments().put("source",originalDisplay),finalDisplay);
                final float OMEGA=10.0F;
                final float BLUR_DIST=60.0F;

                final float THRESHOLD_LEN=0.8F;



                threshold.run(new ShaderArguments().put("source",finalDisplay).put("THRESHOLD_LEN",THRESHOLD_LEN),temp);
                copy.run(new ShaderArguments().put("source",temp),finalDisplay);

                blur.run(new ShaderArguments().put("source",finalDisplay).put("BLUR_DIST",BLUR_DIST).put("OMEGA",OMEGA).put("IS_HORIZONTAL",1.0F),temp);
                copy.run(new ShaderArguments().put("source",temp),finalDisplay);

                blur.run(new ShaderArguments().put("source",finalDisplay).put("BLUR_DIST",BLUR_DIST).put("OMEGA",OMEGA).put("IS_HORIZONTAL",-1.0F),temp);
                copy.run(new ShaderArguments().put("source",temp),finalDisplay);

                recombine.run(new ShaderArguments().put("original",originalDisplay).put("bloom",finalDisplay));

//                copy.run(new ShaderArguments().put("source",finalDisplay));

//                threshold.run(new ShaderArguments().put("source",finalDisplay).put("THRESHOLD_LEN",0.9F));
//                blur.run(new ShaderArguments().put("source",finalDisplay).put("BLUR_DIST",10));
//                copy.run(new ShaderArguments().put("source",temp),finalDisplay);

//                copy.run(new ShaderArguments().put("source",finalDisplay));
//                threshold.run(new ShaderArguments().put(""));
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
            lmx=mouseX;
            lmy=mouseY;
        }

        glfwTerminate();

    }

}
