package co.megadodo.glfluid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static org.lwjgl.opengl.GL41.*;

public class GridData {

    public int id;
    public int color;

    public GridData(){
        color=glGenTextures();
        glBindTexture(GL_TEXTURE_2D,color);
//        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        byte[]ba=new byte[Main.SIMW*Main.SIMH*4];
        Arrays.fill(ba,(byte)0);
        ByteBuffer bb=ByteBuffer.allocateDirect(ba.length).order(ByteOrder.nativeOrder());
        bb.put(ba).flip();
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA32F,Main.SIMW,Main.SIMH,0,GL_RGBA,GL_UNSIGNED_BYTE,bb);

        id=glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,id);

        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,color,0);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER)!=GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Cannot complete framebuffer.");
        }

        glBindFramebuffer(GL_FRAMEBUFFER,0);
    }


}
