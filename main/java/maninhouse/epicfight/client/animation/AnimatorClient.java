package maninhouse.epicfight.client.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maninhouse.epicfight.animation.AnimationPlayer;
import maninhouse.epicfight.animation.Animator;
import maninhouse.epicfight.animation.Joint;
import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.animation.Pose;
import maninhouse.epicfight.animation.types.MirrorAnimation;
import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.capabilities.entity.LivingData.EntityState;
import maninhouse.epicfight.client.ClientEngine;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;

public class AnimatorClient extends Animator
{
	private final Map<LivingMotion, StaticAnimation> livingAnimations = new HashMap<LivingMotion, StaticAnimation>();
	private Map<LivingMotion, StaticAnimation> defaultLivingAnimations;
	private List<LivingMotion> modifiedLivingMotions;
	public final BaseLayer baseLayer;
	public final MixLayer mixLayer;
	private LivingMotion currentMotion;
	private LivingMotion currentMixMotion;
	public boolean reversePlay = false;
	public boolean mixLayerActivated = false;
	
	public AnimatorClient(LivingData<?> entitydata)
	{
		this.entitydata = entitydata;
		this.baseLayer = new BaseLayer(Animations.DUMMY_ANIMATION);
		this.mixLayer = new MixLayer(Animations.DUMMY_ANIMATION);
		this.currentMotion = LivingMotion.IDLE;
		this.currentMixMotion = LivingMotion.NONE;
		this.defaultLivingAnimations = new HashMap<LivingMotion, StaticAnimation> ();
		this.modifiedLivingMotions = new ArrayList<LivingMotion> ();
	}
	
	/** Play an animation by animation id **/
	@Override
	public void playAnimation(int id, float modifyTime)
	{
		this.playAnimation(Animations.findAnimationDataById(id), modifyTime);
	}
	
	/** Play an animation by animation instance **/
	@Override
	public void playAnimation(StaticAnimation nextAnimation, float modifyTime)
	{
		this.baseLayer.pause = false;
		this.mixLayer.pause = false;
		this.reversePlay = false;
		this.baseLayer.playAnimation(nextAnimation, entitydata, modifyTime);
	}
	
	@Override
	public void vacateCurrentPlay()
	{
		baseLayer.animationPlayer.setPlayAnimation(Animations.DUMMY_ANIMATION);
	}
	
	/** Add new Living animation of entity **/
	public void addLivingAnimation(LivingMotion motion, StaticAnimation animation)
	{
		livingAnimations.put(motion, animation);
		
		if(motion == this.currentMotion)
		{
			if(!entitydata.isInaction())
			{
				playAnimation(animation, 0);
			}
		}
	}
	
	public void addLivingMixAnimation(LivingMotion motion, StaticAnimation animation)
	{
		livingAnimations.put(motion, animation);
		
		if(motion == this.currentMotion)
		{
			if(!entitydata.isInaction())
			{
				if(animation instanceof MirrorAnimation)
				{
					playMixLayerAnimation(((MirrorAnimation)animation).checkHandAndReturnAnimation(this.entitydata.getOriginalEntity().getActiveHand()));
				}
				else
				{
					playMixLayerAnimation(animation);
				}
			}
		}
	}
	
	public void addModifiedLivingMotion(LivingMotion motion, StaticAnimation animation)
	{
		if(!modifiedLivingMotions.contains(motion))
		{
			this.modifiedLivingMotions.add(motion);
		}
		
		this.addLivingAnimation(motion, animation);
	}
	
	public void resetModifiedLivingMotions()
	{
		if(modifiedLivingMotions != null)
		{
			for(LivingMotion livingMotion : modifiedLivingMotions)
			{
				this.addLivingAnimation(livingMotion, defaultLivingAnimations.get(livingMotion));
			}
			
			modifiedLivingMotions.clear();
		}
	}
	
	public void setCurrentLivingMotionsToDefault()
	{
		this.defaultLivingAnimations.clear();
		this.defaultLivingAnimations.putAll(this.livingAnimations);
	}
	
	public void playLoopMotion()
	{
		this.currentMotion = entitydata.currentMotion;
		if(livingAnimations.containsKey(entitydata.currentMotion))
			this.baseLayer.playAnimation(livingAnimations.get(entitydata.currentMotion), entitydata, 0);
	}
	
	public void playMixLoopMotion()
	{
		if(entitydata.currentMixMotion == LivingMotion.NONE)
			this.offMixLayer(false);
		else
		{
			StaticAnimation animation = livingAnimations.get(entitydata.currentMixMotion);
			
			if(animation instanceof MirrorAnimation)
				this.playMixLayerAnimation(((MirrorAnimation)animation).checkHandAndReturnAnimation(this.entitydata.getOriginalEntity().getActiveHand()));
			else
				this.playMixLayerAnimation(animation);
		}
		this.mixLayer.pause = false;
		this.currentMixMotion = this.entitydata.currentMixMotion;
	}
	
	public void playMixLayerAnimation(int id)
	{
		playMixLayerAnimation(Animations.findAnimationDataById(id));
	}
	
	public void playMixLayerAnimation(StaticAnimation nextAnimation)
	{
		if(!mixLayerActivated)
		{
			mixLayerActivated = true;
			mixLayer.animationPlayer.synchronize(baseLayer.animationPlayer);
		}
		mixLayer.linkEndPhase = false;
		mixLayer.playAnimation(nextAnimation, entitydata, 0);
	}
	
	public void offMixLayer(boolean byForce)
	{
		if(mixLayerActivated && (byForce || this.mixLayer.animationPlayer.getPlay().getState(this.mixLayer.animationPlayer.getElapsedTime()) != EntityState.POST_DELAY))
		{
			mixLayer.linkEndPhase = true;
			mixLayer.setMixLinkAnimation(entitydata, 0);
			mixLayer.playAnimation(mixLayer.mixLinkAnimation, entitydata);
			mixLayer.nextPlaying = null;
			mixLayer.pause = false;
		}
	}
	
	public void disableMixLayer()
	{
		mixLayerActivated = false;
		
		if(mixLayer.animationPlayer.getPlay()!=null)
		{
			mixLayer.animationPlayer.getPlay().onFinish(entitydata, true);
			mixLayer.animationPlayer.setEmpty();
		}
		
		mixLayer.animationPlayer.resetPlayer();
	}
	
	public void setPoseToModel()
	{
		if(mixLayerActivated)
			applyPoseToJoint(getCurrentPose(baseLayer), getCurrentPose(mixLayer), entitydata.getEntityModel(Models.LOGICAL_CLIENT).getArmature().getJointHierarcy(), new VisibleMatrix4f());
		else
			applyPoseToJoint(getCurrentPose(baseLayer), entitydata.getEntityModel(Models.LOGICAL_CLIENT).getArmature().getJointHierarcy(), new VisibleMatrix4f());
	}
	
	private void applyPoseToJoint(Pose base, Pose mix, Joint joint, VisibleMatrix4f parentTransform)
	{
		if(this.mixLayer.jointMasked(joint.getName()))
		{
			VisibleMatrix4f currentLocalTransformBase = base.getTransformByName(joint.getName()).toTransformMatrix();
			VisibleMatrix4f.mul(joint.getLocalTrasnform(), currentLocalTransformBase, currentLocalTransformBase);
			VisibleMatrix4f bindTransformBase = VisibleMatrix4f.mul(parentTransform, currentLocalTransformBase, null);
			
			VisibleMatrix4f currentLocalTransformMix = mix.getTransformByName(joint.getName()).toTransformMatrix();
			VisibleMatrix4f.mul(joint.getLocalTrasnform(), currentLocalTransformMix, currentLocalTransformMix);
			VisibleMatrix4f bindTransformMix = VisibleMatrix4f.mul(parentTransform, currentLocalTransformMix, null);
			
			bindTransformMix.m31 = bindTransformBase.m31;
			joint.setAnimatedTransform(bindTransformMix);
			
			for(Joint joints : joint.getSubJoints())
			{
				if(this.mixLayer.jointMasked(joints.getName()) || this.currentMotion == LivingMotion.IDLE)
					applyPoseToJoint(mix, joints, bindTransformMix);
				else
					applyPoseToJoint(base, joints, bindTransformBase);
			}
		}
		else
		{
			VisibleMatrix4f currentLocalTransform = base.getTransformByName(joint.getName()).toTransformMatrix();
			VisibleMatrix4f.mul(joint.getLocalTrasnform(), currentLocalTransform, currentLocalTransform);
			VisibleMatrix4f bindTransform = VisibleMatrix4f.mul(parentTransform, currentLocalTransform, null);
			joint.setAnimatedTransform(bindTransform);
			
			for(Joint joints : joint.getSubJoints())
				applyPoseToJoint(base, mix, joints, bindTransform);
		}
	}
	
	private void applyPoseToJoint(Pose pose, Joint joint, VisibleMatrix4f parentTransform)
	{
		VisibleMatrix4f currentLocalTransform = pose.getTransformByName(joint.getName()).toTransformMatrix();
		VisibleMatrix4f.mul(joint.getLocalTrasnform(), currentLocalTransform, currentLocalTransform);
		VisibleMatrix4f bindTransform = VisibleMatrix4f.mul(parentTransform, currentLocalTransform, null);
		VisibleMatrix4f.mul(bindTransform, joint.getAnimatedTransform(), bindTransform);
		joint.setAnimatedTransform(bindTransform);
		
		for(Joint joints : joint.getSubJoints())
		{
			applyPoseToJoint(pose, joints, bindTransform);
		}
	}
	
	public void update()
	{
		if(pause) return;
		baseLayer.update(entitydata, reversePlay);
		if(baseLayer.animationPlayer.isEnd())
		{
			if(baseLayer.nextPlaying == null)
			{
				this.entitydata.updateMotion();
				playLoopMotion();
			}
		}
		
		if(mixLayerActivated)
		{
			mixLayer.update(entitydata, false);
			if(mixLayer.animationPlayer.isEnd())
			{
				if(mixLayer.linkEndPhase)
				{
					if(!pause && mixLayer.nextPlaying == null)
					{
						disableMixLayer();
						mixLayer.linkEndPhase = false;
					}
				}
				else
				{
					mixLayer.animationPlayer.getPlay().onFinish(entitydata, mixLayer.animationPlayer.isEnd());
					if(!mixLayer.pause)
					{
						mixLayer.setMixLinkAnimation(entitydata, 0);
						mixLayer.playAnimation(mixLayer.mixLinkAnimation, entitydata);
						mixLayer.linkEndPhase = true;
					}
				}
			}
		}
	}
	
	@Override
	public void playDeathAnimation()
	{
		this.playAnimation(livingAnimations.get(LivingMotion.DEATH), 0);
	}
	
	public StaticAnimation getJumpAnimation()
	{
		return this.livingAnimations.get(LivingMotion.JUMPING);
	}
	
	public StaticAnimation getDeathAnimation()
	{
		return this.livingAnimations.get(LivingMotion.DEATH);
	}
	
	@Override
	public void onEntityDeath()
	{
		baseLayer.clear(entitydata);
		mixLayer.clear(entitydata);
	}
	
	public Pose getCurrentPose(BaseLayer layer)
	{
		return layer.animationPlayer.getCurrentPose(entitydata, baseLayer.pause ? 1 : ClientEngine.INSTANCE.renderEngine.getPartialTicks());
	}
	
	public boolean compareMotion(LivingMotion motion)
	{
		return this.currentMotion == motion;
	}
	
	public boolean compareMixMotion(LivingMotion motion)
	{
		return this.currentMixMotion == motion;
	}
	
	public void resetMotion()
	{
		this.currentMotion = LivingMotion.IDLE;
	}
	
	public void resetMixMotion()
	{
		this.currentMixMotion = LivingMotion.NONE;
	}
	
	public boolean prevAiming()
	{
		return this.currentMixMotion == LivingMotion.AIMING;
	}
	
	public void playReboundAnimation()
	{
		this.playMixLayerAnimation(this.livingAnimations.get(LivingMotion.SHOTING));
		this.entitydata.resetLivingMixLoop();
	}
	
	@Override
	public AnimationPlayer getPlayer()
	{
		return this.baseLayer.animationPlayer;
	}
	
	@Override
	public AnimationPlayer getPlayerFor(StaticAnimation animation)
	{
		AnimationPlayer player = this.baseLayer.animationPlayer;
		
		if(player.getPlay().equals(animation))
			return player;
		else
			player = this.mixLayer.animationPlayer;
		
		if(player.getPlay().equals(animation))
			return player;
		else
			return null;
	}
	
	public AnimationPlayer getMixLayerPlayer()
	{
		return this.mixLayer.animationPlayer;
	}
}