import ij.*;
import ij.process.*;
import ij.plugin.filter.*;
import static ij.plugin.filter.PlugInFilter.DOES_8G;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class threadingg_1 implements PlugInFilter
{
    long startTime, nullLoopTime;
    int numLoops;
    ExecutorService executor = Executors.newFixedThreadPool(5);
    ImagePlus imp;

    @Override
    public int setup(String arg, ImagePlus imp)
    {
        this.imp = imp;
        return IJ.setupDialog(imp,DOES_8G);
    }
    
    @Override
    public void run(ImageProcessor ip) 
    {        
        
        
        int w = ip.getWidth(), h = ip.getHeight();
        byte[] pixels = (byte[])ip.getPixels();
        int[][] pixelsM = new int[h][w];
        int a,y,x;
        startTime = System.currentTimeMillis();
        for(a=y=0;y<h;y++)
        {   
            executor.execute(new threading23(a, pixels));
            for(x=0;x<w;x++)
                pixelsM[y][x]=pixels[a++]&0xff;
        }
        
        int[] mask={1,1,1,1,1,1,1,1,1};
        int xx,yy;
        double sum=0;
        for(y=1;y<h-1;y++)
        {   
            for(x=1;x<w-1;x++)
            {  
                for(sum=a=0,yy=-1;yy<=1;yy++)
                {   
                    for(xx=-1;xx<=1;xx++)
                        sum+=pixelsM[y+yy][x+xx]*mask[a++];
                }
                pixels[y*w+x]=(byte)(sum/9.+0.5);
            }
                    
        }
        executor.shutdown();
        long elapsedTime = System.currentTimeMillis() - startTime;
	IJ.log(elapsedTime + " ms");
        imp.updateAndDraw();
        
    }
}

class threading23 extends Thread
{
    int i;
    byte[] pixels;

    public threading23(int i, byte[] pixels) {
        this.i = i;
        this.pixels = pixels;
    }
    
    public static void main(String[] args)
    {
        
    }
}
