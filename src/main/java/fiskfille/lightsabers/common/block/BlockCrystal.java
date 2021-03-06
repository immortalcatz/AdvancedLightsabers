package fiskfille.lightsabers.common.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.lightsabers.common.helper.LightsaberColors;
import fiskfille.lightsabers.common.helper.LightsaberHelper;
import fiskfille.lightsabers.common.tileentity.TileEntityCrystal;

public class BlockCrystal extends BlockBasic implements ITileEntityProvider
{	
	private Random rand = new Random();
	
	public BlockCrystal()
	{
		super(Material.glass);
		setLightLevel(0.25F);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(Block.soundTypeGlass);
	}
	
	public boolean canHarvestBlock(EntityPlayer player, int meta)
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List subBlocks)
    {
		for (int i = 0; i < LightsaberColors.getColors().length; ++i)
		{
			subBlocks.add(LightsaberHelper.createCrystal(i));
		}
    }
    
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }
	
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	public int getRenderType()
	{
		return -1;
	}
	
	public boolean isOpaqueCube()
	{
		return false;
	}
	
    public boolean hasTileEntity()
    {
        return true;
    }
    
    private boolean func_150107_m(World world, int x, int y, int z)
    {
        if (World.doesBlockHaveSolidTopSurface(world, x, y, z))
        {
            return true;
        }
        else
        {
            Block block = world.getBlock(x, y, z);
            return block.canPlaceTorchOnTop(world, x, y, z);
        }
    }
    
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return world.isSideSolid(x - 1, y, z, EAST,  true) ||
               world.isSideSolid(x + 1, y, z, WEST,  true) ||
               world.isSideSolid(x, y, z - 1, SOUTH, true) ||
               world.isSideSolid(x, y, z + 1, NORTH, true) ||
               func_150107_m(world, x, y - 1, z) ||
               func_150107_m(world, x, y + 1, z);
    }
    
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        int j1 = metadata;

        if (side == 0 && func_150107_m(world, x, y + 1, z))
        {
            j1 = 6;
        }
        
        if (side == 1 && func_150107_m(world, x, y - 1, z))
        {
            j1 = 5;
        }

        if (side == 2 && world.isSideSolid(x, y, z + 1, NORTH, true))
        {
            j1 = 4;
        }

        if (side == 3 && world.isSideSolid(x, y, z - 1, SOUTH, true))
        {
            j1 = 3;
        }

        if (side == 4 && world.isSideSolid(x + 1, y, z, WEST, true))
        {
            j1 = 2;
        }

        if (side == 5 && world.isSideSolid(x - 1, y, z, EAST, true))
        {
            j1 = 1;
        }
        
        return j1;
    }
    
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        super.updateTick(world, x, y, z, rand);

        if (world.getBlockMetadata(x, y, z) == 0)
        {
            onBlockAdded(world, x, y, z);
        }
    }
    
    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (world.getBlockMetadata(x, y, z) == 0)
        {
            if (world.isSideSolid(x - 1, y, z, EAST, true))
            {
                world.setBlockMetadataWithNotify(x, y, z, 1, 2);
            }
            else if (world.isSideSolid(x + 1, y, z, WEST, true))
            {
                world.setBlockMetadataWithNotify(x, y, z, 2, 2);
            }
            else if (world.isSideSolid(x, y, z - 1, SOUTH, true))
            {
                world.setBlockMetadataWithNotify(x, y, z, 3, 2);
            }
            else if (world.isSideSolid(x, y, z + 1, NORTH, true))
            {
                world.setBlockMetadataWithNotify(x, y, z, 4, 2);
            }
            else if (func_150107_m(world, x, y - 1, z))
            {
                world.setBlockMetadataWithNotify(x, y, z, 5, 2);
            }
            else if (func_150107_m(world, x, y + 1, z))
            {
                world.setBlockMetadataWithNotify(x, y, z, 6, 2);
            }
        }

        func_150109_e(world, x, y, z);
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        func_150108_b(world, x, y, z, block);
    }

    protected boolean func_150108_b(World world, int x, int y, int z, Block block)
    {
        if (func_150109_e(world, x, y, z))
        {
            int l = world.getBlockMetadata(x, y, z);
            boolean flag = false;

            if (!world.isSideSolid(x - 1, y, z, EAST, true) && l == 1)
            {
                flag = true;
            }

            if (!world.isSideSolid(x + 1, y, z, WEST, true) && l == 2)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y, z - 1, SOUTH, true) && l == 3)
            {
                flag = true;
            }

            if (!world.isSideSolid(x, y, z + 1, NORTH, true) && l == 4)
            {
                flag = true;
            }

            if (!func_150107_m(world, x, y - 1, z) && l == 5)
            {
                flag = true;
            }
            
            if (!func_150107_m(world, x, y + 1, z) && l == 6)
            {
                flag = true;
            }

            if (flag)
            {
                dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    protected boolean func_150109_e(World world, int x, int y, int z)
    {
        if (!canPlaceBlockAt(world, x, y, z))
        {
            if (world.getBlock(x, y, z) == this)
            {
                dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }

            return false;
        }
        else
        {
            return true;
        }
    }
    
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vec3, Vec3 vec31)
    {
    	int l = world.getBlockMetadata(x, y, z);
    	float f = 0.0625F;
    	float width = f * 6;
    	float height = f * 6;
    	
    	if (l == 1)
    	{
    		setBlockBounds(0, 0.5F - width / 2, 0.5F - width / 2, height, 0.5F + width / 2, 0.5F + width / 2);
    	}
    	else if (l == 2)
    	{
    		setBlockBounds(1 - height, 0.5F - width / 2, 0.5F - width / 2, 1, 0.5F + width / 2, 0.5F + width / 2);
    	}
    	else if (l == 3)
    	{
    		setBlockBounds(0.5F - width / 2, 0.5F - width / 2, 0, 0.5F + width / 2, 0.5F + width / 2, height);
    	}
    	else if (l == 4)
    	{
    		setBlockBounds(0.5F - width / 2, 0.5F - width / 2, 1 - height, 0.5F + width / 2, 0.5F + width / 2, 1);
    	}
    	else if (l == 5)
    	{
    		setBlockBounds(0.5F - width / 2, 0, 0.5F - width / 2, 0.5F + width / 2, height, 0.5F + width / 2);
    	}
    	else
    	{
    		setBlockBounds(0.5F - width / 2, 1 - height, 0.5F - width / 2, 0.5F + width / 2, 1, 0.5F + width / 2);
    	}
    	
        return super.collisionRayTrace(world, x, y, z, vec3, vec31);
    }
	
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileEntityCrystal();
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemstack)
	{
		super.onBlockPlacedBy(world, x, y, z, entity, itemstack);
		
		TileEntityCrystal tile = (TileEntityCrystal)world.getTileEntity(x, y, z);
		
		if (tile != null)
		{
			tile.setColor(LightsaberHelper.getCrystalColorId(itemstack));
		}
	}
	
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
	{
		return super.getPickBlock(target, world, x, y, z, player);
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		TileEntityCrystal tile = (TileEntityCrystal)world.getTileEntity(x, y, z);
		
		if (tile != null)
		{
	        return LightsaberHelper.createCrystal(tile.colorId);
		}
		
		return super.getPickBlock(target, world, x, y, z);
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float hitX, float hitY, float hitZ)
	{
		TileEntityCrystal tile = (TileEntityCrystal)world.getTileEntity(x, y, z);

		if (tile != null && !player.capabilities.isCreativeMode)
		{
			dropBlockAsItem(world, x, y, z, LightsaberHelper.createCrystal(tile.colorId));
			world.setBlock(x, y, z, Blocks.air);
			return true;
		}
		
		return false;
	}
	
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return 0;
	}
}
