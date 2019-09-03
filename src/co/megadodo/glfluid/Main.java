package co.megadodo.glfluid;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL41.*;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    public static Mesh quad;
    public static int SIMW=2048/2;
    public static int SIMH=2048/2;

    public static void main(String[]__){
        GLFWErrorCallback.createPrint(System.err).set();

        glfwInit();

        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER,GLFW_FALSE);

        long window=glfwCreateWindow(SIMW/2,SIMH/2,"GL Fluid",0,0);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();

        System.out.println("LWJGL VERSION: "+ Version.getVersion());
        System.out.println("OPENGL VERSION: "+glGetInteger(GL_MAJOR_VERSION)+"."+glGetInteger(GL_MINOR_VERSION));

        quad=new Mesh();
        quad.addBuffer2f(0,new float[]{
                -1,-1,
                -1,1,
                1,-1,
                1,1
        });
        quad.addBuffer2f(1,new float[]{
                0,0,
                0,SIMH,
                SIMW,0,
                SIMW,SIMH
        });
        quad.setIndices(new int[]{
                0,1,3,
                0,2,3
        });

        GridData[] density =new GridData[]{new GridData(),new GridData()};
        GridData velocity=new GridData();
        Computation display=new Computation("display");
        Computation initDensity=new Computation("init_uv");
        Computation initVelocity=new Computation("init_velocity");

        Computation advection=new Computation("advect");

        glClearColor(1,1,1,1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        initDensity.run(new ShaderArguments(),density[0]);
        initVelocity.run(new ShaderArguments(),velocity);



        /*
        init.run(args=none,target=gridData)

        LOOP:
        display.run(args=(tex=gridData),
         */

        while(!glfwWindowShouldClose(window)){
            glClearColor(1,1,1,1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//            glViewport(0,0,SIMW/2,SIMH/2);

            {
                ShaderArguments args=new ShaderArguments();
                args.put("density",density[0]);
                args.put("velocity",velocity);
                advection.run(args,density[1]);
            }
            {
                ShaderArguments args=new ShaderArguments();
                args.put("density",density[1]);
                args.put("velocity",velocity);
                advection.run(args,density[0]);
            }

            ShaderArguments args=new ShaderArguments();
            args.put("density",density[0]);
            args.put("velocity",velocity);
            display.run(args);


            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        glfwTerminate();

    }

}
