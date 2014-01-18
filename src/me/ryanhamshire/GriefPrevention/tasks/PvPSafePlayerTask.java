package me.ryanhamshire.GriefPrevention.tasks;

import me.ryanhamshire.GriefPrevention.Configuration.WorldConfig;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import me.ryanhamshire.GriefPrevention.PlayerData;
import me.ryanhamshire.GriefPrevention.TextMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

//used to alert when a player is safe to logout without invoking the wrath of the PVP logout protection.
public class PvPSafePlayerTask implements Runnable{

    private Player CheckSafetyPlayer;
    private int TickTimeout;
    WorldConfig originalconfig = null;
    public static ArrayList<PvPSafePlayerTask> RunningTasks = new ArrayList<PvPSafePlayerTask>();
    public PvPSafePlayerTask(Player checkplayer,int Timeout){
        CheckSafetyPlayer = checkplayer;
        TickTimeout = Timeout;
        originalconfig = GriefPrevention.instance.getWorldCfg(checkplayer.getWorld());
        Bukkit.getScheduler().runTaskLater(GriefPrevention.instance,this,TickTimeout);
        System.out.println("run: PvPSafePlayerTask, Ticks: " + TickTimeout);
    }
    public void run()
    {
        System.out.println("run: PvPSafePlayerTask");
        PlayerData connectedData = GriefPrevention.instance.dataStore.getPlayerData(CheckSafetyPlayer.getName());
            if(connectedData.inPvpCombat()){
                //reschedule...
                System.out.println("still in PvP Combat...");
                Bukkit.getScheduler().runTaskLater(GriefPrevention.instance,this,TickTimeout);

            }
            else{
                //display safety message.
                System.out.println("displaying safety message...");
                GriefPrevention.sendMessage(CheckSafetyPlayer, TextMode.Info, Messages.PvPLogoutSafely);
                RunningTasks.remove(this);
            }



    }


}