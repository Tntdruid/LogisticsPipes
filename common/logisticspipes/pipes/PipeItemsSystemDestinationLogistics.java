package logisticspipes.pipes;

import java.util.UUID;

import logisticspipes.interfaces.ILogisticsModule;
import logisticspipes.logic.DestinationLogic;
import logisticspipes.pipefxhandlers.Particles;
import logisticspipes.pipes.basic.RoutedPipe;
import logisticspipes.proxy.MainProxy;
import logisticspipes.textures.Textures;
import logisticspipes.textures.Textures.TextureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import buildcraft.core.utils.SimpleInventory;

public class PipeItemsSystemDestinationLogistics extends RoutedPipe {

	public SimpleInventory inv = new SimpleInventory(1, "Freq Slot", 1);
	
	public PipeItemsSystemDestinationLogistics(int itemID) {
		super(new DestinationLogic(), itemID);
	}

	@Override
	public ItemSendMode getItemSendMode() {
		return ItemSendMode.Normal;
	}

	@Override
	public TextureType getCenterTexture() {
		return Textures.LOGISTICSPIPE_DESTINATION_TEXTURE;
	}

	@Override
	public ILogisticsModule getLogisticsModule() {
		return null;
	}

	public Object getTargetUUID() {
		if(inv.getStackInSlot(0) == null) return null;
		if(!inv.getStackInSlot(0).hasTagCompound()) return null;
		if(!inv.getStackInSlot(0).getTagCompound().hasKey("UUID")) return null;
		MainProxy.sendSpawnParticlePacket(Particles.VioletParticle, xCoord, yCoord, this.zCoord, this.worldObj, 2);
		return UUID.fromString(inv.getStackInSlot(0).getTagCompound().getString("UUID"));
	}

	@Override
	public void onBlockRemoval() {
		dropFreqCard();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		inv.writeToNBT(nbttagcompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		inv.readFromNBT(nbttagcompound);
	}

	private void dropFreqCard() {
		if(inv.getStackInSlot(0) == null) return;
		EntityItem item = new EntityItem(worldObj,this.xCoord, this.yCoord, this.zCoord, inv.getStackInSlot(0));
		worldObj.spawnEntityInWorld(item);
		inv.setInventorySlotContents(0, null);
	}
}
