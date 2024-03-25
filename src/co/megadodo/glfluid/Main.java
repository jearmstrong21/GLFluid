package co.megadodo.glfluid;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL41.*;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    public static int SIMW = 1024;
    public static int SIMH = 1024;
    public static int WINW = 1500;
    public static int WINH = 1500;

    public static float TIME_MULT = 0.0025f;
    public static float RHO = 0.1F;
    public static float DT = 0.1F;
    public static float EPSILON = 1.0F / SIMW;
    public static float dt = 0;

    ShaderArguments args() {
        return new ShaderArguments().put("texSize", SIMW, SIMH).put("dt", dt * TIME_MULT).put("RHO", RHO).put("EPSILON", EPSILON);
    }

    public static long makeWindow() {
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

        return window;
    }

    Computation advect;
    Computation copy;

    void copy(GridData source, GridData to) {
        copy.run(args().put("source", source), to);
    }

    void advectInto(GridData data, GridData velocity, GridData to) {
        advect.run(args().put("source", data).put("velocity", velocity), to);
    }

    public long window;

    Main() {
        window = makeWindow();
        Mesh.makeMesh();

        GridData density = new GridData();
        GridData velocity = new GridData();

        GridData temp = new GridData();
        GridData temp2 = new GridData();
        GridData divergence = new GridData();
        GridData pressure = new GridData();

        Computation display = new Computation("display");
        Computation initDensity = new Computation("init_density");
        Computation initVelocity = new Computation("init_velocity");
        advect = new Computation("advect");
        copy = new Computation("copy");
        Computation calcDivergence = new Computation("divergence");
        Computation pressureIteration = new Computation("pressure_iteration");
        Computation allZero = new Computation("all_zero");
        Computation fixPressure = new Computation("fix_pressure");
        Computation velocityMouse = new Computation("velocity_mouse");
        Computation clampAboveZero = new Computation("clamp_above_zero");
        Computation densityMouse = new Computation("density_mouse");

        Computation blur = new Computation("postprocess/blur");
        Computation threshold = new Computation("postprocess/threshold");
        Computation recombine = new Computation("postprocess/recombine");

//        Computation drawVelocity=new Computation("draw_velocity");

        GridData originalDisplay = new GridData();

        glClearColor(1, 1, 1, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        initDensity.run(args(), density);
        initVelocity.run(args(), velocity);

        float lmx = 0, lmy = 0;

        long lastTime = System.currentTimeMillis();

        while (!glfwWindowShouldClose(window)) {
            long newTime = System.currentTimeMillis();
            dt = (newTime - lastTime) / 1000.0f;
            lastTime = newTime;

            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float mouseX, mouseY;
            {
                double[] mx = new double[1], my = new double[1];
                glfwGetCursorPos(window, mx, my);
                mouseX = (float) mx[0];
                mouseY = (float) my[0];
            }
//            mouseY=WINH-mouseY;
            mouseX *= (float) SIMW / WINW;
            mouseY *= (float) SIMH / WINH;
            mouseX *= 2;
            mouseY *= 2;
            mouseY = SIMH - mouseY;

            clampAboveZero.run(args().put("source", density), temp);
            copy.run(args().put("source", temp), density);

            if (glfwGetKey(window, GLFW_KEY_1) == GLFW_PRESS) {
                velocityMouse.run(args().put("velocity", velocity).put("mousePos", mouseX, mouseY).put("mouseDir", mouseX - lmx, mouseY - lmy), temp);
                copy(temp, velocity);
            }
            if (glfwGetKey(window, GLFW_KEY_2) == GLFW_PRESS) {
                densityMouse.run(args().put("density", density).put("mousePos", mouseX, mouseY).put("change",
                        glfwGetKey(window, GLFW_KEY_3) == GLFW_PRESS ? 1.0F : -1.0F
                ), temp);
                copy(temp, density);
            }

            advectInto(density, velocity, temp);
            copy(temp, density);

            advectInto(velocity, velocity, temp);
            copy(temp, velocity);

            clampAboveZero.run(args().put("source", density), temp);
            copy(temp, density);


            allZero.run(new ShaderArguments(), pressure);
            for (int i = 0; i < 100; i++) {
                calcDivergence.run(args().put("velocity", velocity), divergence);
                pressureIteration.run(args().put("divergence", divergence).put("last_pressure", pressure), temp);
                copy(temp, pressure);
            }

            fixPressure.run(args().put("pressure", pressure).put("velocity", velocity), temp);
            copy(temp, velocity);


            {
                display.run(args().put("density", density), originalDisplay);
                final float OMEGA = 10.0F;
                final float BLUR_DIST = 80.0F;

                final float THRESHOLD_LEN = 0.9F;

                threshold.run(args().put("source", originalDisplay).put("THRESHOLD_LEN", THRESHOLD_LEN), temp);
                copy(temp, null);
                blur.run(args().put("source", temp).put("BLUR_DIST", BLUR_DIST).put("OMEGA", OMEGA).put("IS_HORIZONTAL", 1.0F), temp2);
                blur.run(args().put("source", temp2).put("BLUR_DIST", BLUR_DIST).put("OMEGA", OMEGA).put("IS_HORIZONTAL", -1.0F), temp);

                // temp = blur
                copy.run(args().put("source", temp));
                recombine.run(args().put("original", originalDisplay).put("bloom", temp));
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
            lmx = mouseX;
            lmy = mouseY;
        }

        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main();
    }

}
