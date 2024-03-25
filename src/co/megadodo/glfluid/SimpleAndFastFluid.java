package co.megadodo.glfluid;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL41.*;

public class SimpleAndFastFluid {

    long window;
    long startTime;

    Computation main;
    Computation init;
    Computation copy;
    Computation display;

    GridData data;
    GridData temp;

//    GridData picture;
//    Computation advect;

    ShaderArguments a() {
        return new ShaderArguments().put("size", Main.SIMW, Main.SIMH).put("time", (System.currentTimeMillis() - startTime) / 1000f);
    }

    SimpleAndFastFluid() {;
        window = Main.makeWindow();
        init = new Computation("simple_and_fast/init");
        main = new Computation("simple_and_fast/main");
        copy = new Computation("copy");
        display = new Computation("simple_and_fast/display");
//        advect = new Computation("advect");
//        picture = new GridData();
        data = new GridData();
        temp = new GridData();
        Mesh.makeMesh();

        init.run(a(), data);
//        new Computation("init_density").run(a().put("texSize", Main.SIMW, Main.SIMH), picture);

        startTime = System.currentTimeMillis();

        while (!glfwWindowShouldClose(window)) {
            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            if (glfwGetKey(window, GLFW_KEY_1) == GLFW_PRESS) {
                init.run(a(), data);
//                new Computation("init_density").run(a().put("texSize", Main.SIMW, Main.SIMH), picture);
            }

            for (int i = 0; i < 50; i++) {
                main.run(a().put("tex", data), temp);
                copy.run(a().put("source", temp), data);
//                advect.run(a().put("texSize", Main.SIMW, Main.SIMH).put("source", picture).put("velocity", data).put("dt", 0.15f / Main.SIMW), temp);
//                copy.run(a().put("source", temp), picture);
            }

            display.run(a().put("tex", data));
//            copy.run(a().put("source", picture));

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        glfwTerminate();
    }

    public static void main(String[] args) {
        new SimpleAndFastFluid();
    }
}