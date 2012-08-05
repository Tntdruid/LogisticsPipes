package net.minecraft.src.buildcraft.logisticspipes.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.SpriteHelper;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.core_LogisticsPipes;
import net.minecraft.src.buildcraft.krapht.LogisticsItem;
import net.minecraft.src.krapht.ItemIdentifier;
import net.minecraft.src.krapht.SimpleInventory;

public abstract class ItemModuleProxy extends LogisticsItem {

	public ItemModuleProxy(int i) {
		super(i);
	}

	public abstract int getModuleIconFromDamage(int damage);

	@Override
	public String getTextureFile() {
		return core_LogisticsPipes.LOGISTICSITEMS_TEXTURE_FILE;
	}
	
	@Override
	public int getIconFromDamage(int i) {
		return getModuleIconFromDamage(i);
	}
	
	@Override
	public boolean func_46056_k() {
		return true;
	}
	
	public abstract String getTextureMap();
	
	public void loadModules() {
		SpriteHelper.registerSpriteMapForFile(core_LogisticsPipes.LOGISTICSITEMS_TEXTURE_FILE, getTextureMap());
	}
	
	public void addInformation(ItemStack itemStack, List list) {
		if(itemStack.hasTagCompound()) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				NBTTagCompound nbt = itemStack.getTagCompound();
				if(nbt.hasKey("informationList")) {
					NBTTagList nbttaglist = nbt.getTagList("informationList");
					for(int i=0;i<nbttaglist.tagCount();i++) {
						NBTBase nbttag = nbttaglist.tagAt(i);
						String data = ((NBTTagString)nbttag).data;
						if(data.equals("<inventory>") && i + 1 < nbttaglist.tagCount()) {
							nbttag = nbttaglist.tagAt(i + 1);
							data = ((NBTTagString)nbttag).data;
							if(data.startsWith("<that>")) {
								String prefix = data.substring(6);
								NBTTagCompound module = nbt.getCompoundTag("moduleInformation");
								SimpleInventory inv = new SimpleInventory(module.getTagList(prefix + "items").tagCount(), "InformationTempInventory", Integer.MAX_VALUE);
								inv.readFromNBT(module, prefix);
								for(int pos=0;pos < inv.getSizeInventory();pos++) {
									ItemStack stack = inv.getStackInSlot(pos);
									if(stack != null) {
										if(stack.stackSize > 1) {
											list.add("  " + stack.stackSize+"x " + ItemIdentifier.get(stack).getFriendlyName());	
										} else {
											list.add("  " + ItemIdentifier.get(stack).getFriendlyName());
										}
									}
								}
							}
							i++;
						} else {
							list.add(data);
						}
					}
				}
			}
			/*if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
				NBTTagCompound nbt = itemStack.getTagCompound();
				list.add(nbt.toString());
				for(Object obj:nbt.getTags().toArray()) {
					list.add(obj.toString());				
				}
			}*/
		}
	}
	
	public abstract String getModuleDisplayName(ItemStack itemstack);
	
	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return getModuleDisplayName(itemstack);
	}
	
}
