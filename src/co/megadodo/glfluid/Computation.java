package co.megadodo.glfluid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL41.*;

public class Computation {

    public int id;

    private static String vertCode;

    static {
        try {
            vertCode = Files.readString(Paths.get("shader/quad.vert"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Computation(String name) {
        int vert = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vert, vertCode);
        glCompileShader(vert);
        if (glGetShaderi(vert, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("ERROR: SHADER " + name + ".vert");
            System.err.println(glGetShaderInfoLog(vert));
            System.exit(1);
        }

        int frag = glCreateShader(GL_FRAGMENT_SHADER);
        try {
            glShaderSource(frag, Files.readString(Paths.get("shader/" + name + ".frag")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        glCompileShader(frag);
        if (glGetShaderi(frag, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("ERROR: SHADER " + name + ".frag");
            System.err.println(glGetShaderInfoLog(frag));
            System.exit(1);
        }

        id = glCreateProgram();
        glAttachShader(id, vert);
        glAttachShader(id, frag);
        glLinkProgram(id);

        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("ERROR: PROGRAM " + name);
            System.err.println(glGetProgramInfoLog(id));
            System.exit(1);
        }
    }

    public void run(ShaderArguments args){
        run(args,null);
    }

    public void run(ShaderArguments args, GridData target){
        if(target!=null){
            glViewport(0,0,Main.SIMW,Main.SIMH);
            glBindFramebuffer(GL_FRAMEBUFFER,target.id);
            for(String s:args.texs.keySet()){
                if(args.texs.get(s).id == target.id){
                    throw new RuntimeException("Can't render with target == data[\""+s+"\"]");
                }
            }
        }else{
            glViewport(0,0,Main.WINW,Main.WINH);
        }
        glUseProgram(id);
        for(String s:args.ints.keySet()){
            glUniform1i(glGetUniformLocation(id,s),args.ints.get(s));
        }
        for(String s:args.floats.keySet()){
            glUniform1f(glGetUniformLocation(id,s),args.floats.get(s));
        }
        for(String s:args.float2s.keySet()){
            glUniform2f(glGetUniformLocation(id,s),args.float2s.get(s).x,args.float2s.get(s).y);
        }
        int i=0;
        for(String s:args.texs.keySet()){
            glUniform1i(glGetUniformLocation(id,s),i);
            glActiveTexture(GL_TEXTURE0+i);
            glBindTexture(GL_TEXTURE_2D,args.texs.get(s).color);
            i++;
        }
        Main.quad.renderElements();
        if(target!=null){
            glBindFramebuffer(GL_FRAMEBUFFER,0);
        }
    }

}
