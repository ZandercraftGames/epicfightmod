package maninhouse.epicfight.client;

import maninhouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninhouse.epicfight.client.events.engine.ControllEngine;
import maninhouse.epicfight.client.events.engine.RenderEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientEngine
{
	public static ClientEngine INSTANCE;
	public Minecraft minecraft;
	public RenderEngine renderEngine;
	public ControllEngine inputController;
	
	private ClientPlayerData playerdata;
	private PlayerActingMode playerActingMode = PlayerActingMode.MINING;
	private PointOfView pointOfViewMemory;
	
	public ClientEngine()
	{
		INSTANCE = this;
		
		minecraft = Minecraft.getInstance();
		renderEngine = new RenderEngine();
		inputController = new ControllEngine();
	}
	
	public void toggleActingMode()
	{
		if(this.playerActingMode == PlayerActingMode.MINING)
			this.switchToBattleMode();
		else
			this.switchToMiningMode();
	}
	
	private void switchToMiningMode()
	{
		this.playerActingMode = PlayerActingMode.MINING;
		this.renderEngine.guiSkillBar.slideDown();
		
		if(this.pointOfViewMemory == PointOfView.FIRST_PERSON)
			minecraft.gameSettings.setPointOfView(PointOfView.FIRST_PERSON);
	}
	
	private void switchToBattleMode()
	{
		this.playerActingMode = PlayerActingMode.BATTLE;
		this.renderEngine.guiSkillBar.slideUp();
		if(minecraft.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON)
			minecraft.gameSettings.setPointOfView(PointOfView.THIRD_PERSON_BACK);
	}
	
	public void setLastPointOfView(PointOfView pov)
	{
		this.pointOfViewMemory = pov;
	}
	
	public PlayerActingMode getPlayerActingMode()
	{
		return this.playerActingMode;
	}
	
	public boolean isBattleMode()
	{
		return this.playerActingMode == PlayerActingMode.BATTLE;
	}
	
	public void setPlayerData(ClientPlayerData playerdata)
	{
		if(this.playerdata != null && this.playerdata != playerdata)
			this.playerdata.discard();
		this.playerdata = playerdata;
	}
	
	public ClientPlayerData getPlayerData()
	{
		if(this.playerdata != null)
			return this.playerdata;
		else
		{
			ClientPlayerData data = new ClientPlayerData();
			data.setEntity(this.minecraft.player);
			return data;
		}
	}
	
	public static enum PlayerActingMode
	{
		MINING, BATTLE
	}
}