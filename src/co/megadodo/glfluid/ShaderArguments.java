package co.megadodo.glfluid;

import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL41.*;

public class ShaderArguments {

    public Map<String,Integer>ints;
    public Map<String,Float>floats;
    public Map<String, Vector2f>float2s;
    public Map<String,GridData>texs;

    public ShaderArguments(){
        ints=new HashMap<>();
        floats=new HashMap<>();
        float2s=new HashMap<>();
        texs=new HashMap<>();
    }

    public void put(String s,int i){
        ints.put(s,i);
    }

    public void put(String s,float f){
        floats.put(s,f);
    }

    public void put(String s,Vector2f f2){
        float2s.put(s,f2);
    }

    public void put(String s,float f21,float f22){
        float2s.put(s,new Vector2f(f21,f22));
    }

    public void put(String s,GridData gdd){
        texs.put(s,gdd);
    }

}
