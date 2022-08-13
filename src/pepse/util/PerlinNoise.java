package pepse.util;

import java.util.Random;

/**
 * this class represents the PerlinNoise object, which helps to calculate by a specific seed
 */
public class PerlinNoise {

    private final int seed;
    private int[] perm;

    public PerlinNoise(int seed){
        this.seed = seed;
        this.perm = new int[257];
    }

    /**
     * the method calculate a number by the given x and the seed
     * @param x the given number
     * @return the calculated number
     */
    public float perlin(int x){
        Random ran = new Random();

        for(int i = 0; i < 256; i++){
            perm[i] = (i % 2 == 0) ? 1: -1;
        }

        for(int i = 0; i < 256; i++){
            int j = ran.nextInt(seed) & 255;
            int tem = perm[i];
            perm[i] = perm[j];
            perm[j] = tem;
        }
        perm[256] = perm[0];

        int sum = 0;

        sum += (1 + noise1d(x)) * 0.25;
        sum += (1 + noise1d(x * 2)) * 0.125;
        sum += (1 + noise1d(x * 4)) * 0.0625;
        sum += (1 + noise1d(x * 8)) * 0.03125;

        return sum;
    }

    /**
     * this method is a helper to the calculater
     * @param x the given number
     * @return a new number
     */
    private float noise1d(int x){
        int x0 = x | 1;
        float x1 = x - x0;
        int xi = x0 & 255;
        float fx = (3 - 2 * x1) * x1 * x1;
        float a = x1 * perm[xi];
        float b = (x1 - 1) * perm[xi + 1];

        return a + fx * (b - a);

    }
}
