package co.megadodo.glfluid;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * A collection of buffers
 * Renderable
 * Renders a bunch of triangles or lines
 */
public class Mesh implements Serializable {

    public static Mesh quad;


    public static void makeMesh() {
        quad = new Mesh();
        quad.addBuffer2f(0, new float[]{
                -1, -1,
                -1, 1,
                1, -1,
                1, 1
        });
        quad.addBuffer2f(1, new float[]{
                0, 0,
                0, 1,
                1, 0,
                1, 1
        });
        quad.setIndices(new int[]{
                0, 1, 3,
                0, 2, 3
        });
    }

    public int id;
    public Map<Integer, Integer> buffers;
    public int ebo;
    public int eboSize;

    public Mesh() {
        buffers = new HashMap<>();

        id = glGenVertexArrays();
        glBindVertexArray(id);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, new int[]{}, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        eboSize = 0;

        glBindVertexArray(0);
    }

    public void setIndices(int[] data) {
        eboSize = data.length;
        glBindVertexArray(id);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public void addBuffer2f(int attrib, float[] data) {
        glBindVertexArray(id);
        int vbo = glGenBuffers();
        buffers.put(attrib, vbo);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glVertexAttribPointer(attrib, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);
        glEnableVertexAttribArray(attrib);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
    }

    public void renderElements() {
        glBindVertexArray(id);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glDrawElements(GL_TRIANGLES, eboSize, GL_UNSIGNED_INT, 0);
    }

}
