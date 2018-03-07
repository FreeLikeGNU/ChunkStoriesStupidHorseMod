package xyz.chunkstories.template;

import io.xol.chunkstories.api.entity.ai.AI;

import java.util.Random;

public class HorseAI extends AI<EntityHorse> {

    private int RoamCooldown = 0;
    private Random random = new Random();

    public HorseAI(EntityHorse entity){
        super(entity);
    }

    public void tick(){
        if (entity.isDead())
            return;


        if (RoamCooldown >0){
            RoamCooldown--;
            return;
        }

        //Generating random X and Y distances for the Horse to roam to
        double rngX = random.nextDouble() * 10;
        double rngY = random.nextDouble() * 10;



    }
}
