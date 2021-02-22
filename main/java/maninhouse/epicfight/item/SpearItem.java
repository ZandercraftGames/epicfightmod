package maninhouse.epicfight.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.capabilities.item.ModWeaponCapability;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Colliders;
import maninhouse.epicfight.gamedata.Skills;
import maninhouse.epicfight.gamedata.Sounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;

public class SpearItem extends WeaponItem
{
	private final ItemTier material;
	private final float attackDamage;
	
	public SpearItem(Item.Properties build, ItemTier materialIn)
	{
		super(build.defaultMaxDamage((int)(materialIn.getMaxUses() * 1.5F)));
		this.material = materialIn;
		this.attackDamage = 3.0F + material.getAttackDamage();
		this.setStats();
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		return this.material.getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
    }
	
	@Override
	public int getItemEnchantability()
    {
        return this.material.getEnchantability();
    }

	@Override
	public boolean canHarvestBlock(BlockState blockIn)
    {
        return false;
    }
    
    @Override
    public void setWeaponCapability()
    {
    	
    }
    
    public void setStats()
    {
    	double oneHandImpact = 2.4D + this.material.getHarvestLevel() * 0.3D;
    	double twoHandImpact = 0.6D + this.material.getHarvestLevel() * 0.5D;
    	
    	capability = new ModWeaponCapability(Skills.SLAUGHTER_STANCE, null, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.spearNarrow,
    			4.0D + 4.0D * this.material.getHarvestLevel(), oneHandImpact, 1, false, true);
    	
    	capability.addAutoAttackCombos(Animations.SPEAR_ONEHAND_AUTO);
    	capability.addAutoAttackCombos(Animations.SPEAR_DASH);
    	capability.addTwohandAutoAttackCombos(Animations.SPEAR_TWOHAND_AUTO_1);
    	capability.addTwohandAutoAttackCombos(Animations.SPEAR_TWOHAND_AUTO_2);
    	capability.addTwohandAutoAttackCombos(Animations.SPEAR_DASH);
    	capability.addMountAttackCombos(Animations.SPEAR_MOUNT_ATTACK);
    	capability.setTwoHandStyleAttribute(0, twoHandImpact, 3);
    	capability.addLivingMotionChanger(LivingMotion.RUNNING, Animations.BIPED_RUN_HELDING_WEAPON);
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
    {
    	if (slot == EquipmentSlotType.MAINHAND)
        {
    		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
    	    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, Operation.ADDITION));
    	    builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.8D, Operation.ADDITION));
    	    return builder.build();
        }
        
        return super.getAttributeModifiers(slot, stack);
    }
}