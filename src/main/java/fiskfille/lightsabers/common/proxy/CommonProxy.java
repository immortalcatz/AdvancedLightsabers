package fiskfille.lightsabers.common.proxy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import fiskfille.lightsabers.Lightsabers;
import fiskfille.lightsabers.common.block.ModBlocks;
import fiskfille.lightsabers.common.entity.ModEntities;
import fiskfille.lightsabers.common.event.CommonEventHandler;
import fiskfille.lightsabers.common.generator.ModChestGen;
import fiskfille.lightsabers.common.item.ModItems;
import fiskfille.lightsabers.common.lightsaber.LightsaberManager;
import fiskfille.lightsabers.common.recipe.ModRecipes;

public class CommonProxy
{
	private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();
	
	public void preInit()
	{
		LightsaberManager.register();
		ModItems.register();
		ModBlocks.register();
		ModRecipes.register();
		ModEntities.register();
		ModChestGen.register();
		
		registerEventHandler(new CommonEventHandler());
		
		if (Lightsabers.isDynamicLightsLoaded)
		{
			fiskfille.lightsabers.common.event.CommonEventHandlerDL eventHandler = new fiskfille.lightsabers.common.event.CommonEventHandlerDL();
			eventHandler.load();
			registerEventHandler(eventHandler);
		}
	}

	public void init()
	{
		
	}
	
	public void registerEventHandler(Object obj)
    {
        MinecraftForge.EVENT_BUS.register(obj);
        FMLCommonHandler.instance().bus().register(obj);
    }

	public EntityPlayer getPlayer()
	{
		return null;
	}
	
	public static void storeEntityData(String name, NBTTagCompound compound)
	{
		extendedEntityData.put(name, compound);
	}

	public static NBTTagCompound getEntityData(String name)
	{
		return extendedEntityData.remove(name);
	}
}
