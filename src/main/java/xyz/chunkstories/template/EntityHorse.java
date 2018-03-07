package xyz.chunkstories.template;


import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.DamageCause;
import io.xol.chunkstories.api.entity.EntityDefinition;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.entity.EntityRenderable;
import io.xol.chunkstories.api.rendering.entity.EntityRenderer;
import io.xol.chunkstories.api.rendering.entity.RenderingIterator;
import io.xol.chunkstories.api.rendering.textures.Texture2D;
import io.xol.chunkstories.api.rendering.world.WorldRenderer;
import io.xol.chunkstories.api.sound.SoundSource;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.core.entity.EntityHumanoid;
import io.xol.chunkstories.core.entity.EntityLivingImplementation;
import io.xol.chunkstories.core.entity.EntityZombie;
import org.joml.Matrix4f;
import io.xol.chunkstories.api.rendering.world.WorldRenderer.RenderingPass;


public abstract class EntityHorse extends EntityLivingImplementation {


    @Override
    public float getStartHealth(){
        return 150;
    }


    public EntityHorse(EntityDefinition t, Location location){
        super(t, location);
    }

    @Override
    public void tick(){
        super.tick();
    }

    @Override
    public float damage(DamageCause cause, HitBox osef, float damage) {
        if (!isDead()) {
            world.getSoundManager().playSoundEffect("sounds/entities/oof.mp3", SoundSource.Mode.NORMAL, this.getLocation(), (float) Math.random() * 0.4f + 0.8f, 1.5f + Math.min(0.5f, damage / 15.0f));
            return super.damage(cause, osef, damage);
        }

        world.getSoundManager().playSoundEffect("sounds/entities/oogh.mp3", SoundSource.Mode.NORMAL, this.getLocation(), (float) Math.random() * 0.4f + 0.8f, 1.5f + Math.min(0.5f, damage / 15.0f));

        return super.damage(cause, osef, damage);
    }

    class EntityHorseRenderer implements EntityRenderer<EntityHorse> {

        void setupRender(RenderingInterface renderingContext) {
            renderingContext.bindAlbedoTexture(renderingContext.textures().getTexture("./shite-horse.png"));
            renderingContext.bindNormalTexture(renderingContext.textures().getTexture("./textures/normalnormal.png"));
            renderingContext.bindMaterialTexture(renderingContext.textures().getTexture("./textures/defaultmaterial.png"));
        }

        @Override
        public int renderEntities(RenderingInterface renderer, RenderingIterator<EntityHorse> renderableEntitiesIterator){
            setupRender(renderer);

            int e = 0;

            for (EntityHorse entity : renderableEntitiesIterator.getElementsInFrustrumOnly())
            {
                Location location = entity.getPredictedLocation();

                if (renderer.getWorldRenderer().getCurrentRenderingPass() == RenderingPass.SHADOW && location.distance(renderer.getCamera().getCameraPosition()) > 15f)
                    continue;

                CellData cell = entity.getWorld().peekSafely(entity.getLocation());
                renderer.currentShader().setUniform2f("worldLightIn", cell.getBlocklight(), cell.getSunlight());



                Matrix4f matrix = new Matrix4f();
                matrix.translate((float)location.x, (float)location.y, (float)location.z);
                renderer.setObjectMatrix(matrix);

                renderer.meshes().getRenderableMultiPartAnimatableMeshByName("./models/horse.obj").render(renderer, entity.getAnimatedSkeleton(), System.currentTimeMillis() % 1000000);
            }
            return e;
        }

        @Override
        public void freeRessources() {

        }
    }

    @Override
    public EntityRenderer<? extends EntityRenderable> getEntityRenderer()
    {
        return new EntityHorse.EntityHorseRenderer();
    }

    public Location getPredictedLocation()
    {
        return getLocation();
    }
}
