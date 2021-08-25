/*
 *
 *                         Copyright (c) 2016-2019
 *                SparklingComet @ http://shanerx.org
 *               KillerOfPie @ http://killerofpie.github.io
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  NOTICE: All modifications made by others to the source code belong
 *  to the respective contributor. No contributor should be held liable for
 *  any damages of any kind, whether be material or moral, which were
 *  caused by their contribution(s) to the project. See the full License for more information.
 *
 */

package org.shanerx.tradeshoparm.listeners;

import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.shanerx.tradeshop.TradeShop;
import org.shanerx.tradeshop.objects.Shop;
import org.shanerx.tradeshop.objects.ShopLocation;
import org.shanerx.tradeshop.utils.data.DataStorage;
import org.shanerx.tradeshoparm.TradeShopARM;
import net.alex9849.arm.events.RestoreRegionEvent;


public class ARMRestoreRegionEventListener implements Listener {

    private final TradeShop tradeShop = (TradeShop) Bukkit.getPluginManager().getPlugin("TradeShop");
    private final TradeShopARM tradeShopARM;

    public ARMRestoreRegionEventListener(TradeShopARM tradeShopARM) {
        this.tradeShopARM = tradeShopARM;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRegionRestore(RestoreRegionEvent event) {
        if(!event.isCancelled()) {
            DataStorage dataStorage = tradeShop.getDataStorage();
            Region region = event.getRegion();
            World world = region.getRegionworld();

            for (Vector point : region.getRegion().getPoints()) {
                ShopLocation sl = new ShopLocation(world, point.getBlockX(), point.getBlockY(), point.getBlockZ());
                Shop shop = dataStorage.loadShopFromSign(sl);
                if (shop != null)
                    shop.remove();
                dataStorage.removeChestLinkage(sl);
            }
        }
    }
}
