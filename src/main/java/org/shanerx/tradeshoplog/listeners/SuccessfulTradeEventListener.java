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

package org.shanerx.tradeshoplog.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shanerx.tradeshop.framework.events.PlayerSuccessfulTradeEvent;
import org.shanerx.tradeshoplog.TradeShopLOG;


public class SuccessfulTradeEventListener implements Listener {

    private final TradeShopLOG tradeShopLOG;

    public SuccessfulTradeEventListener(TradeShopLOG tradeShopARM) {
        this.tradeShopLOG = tradeShopARM;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSuccessfulTrade(PlayerSuccessfulTradeEvent event) {
        if(!event.isCancelled()) {
            tradeShopLOG.getTransactionLogger().logTransaction(event);
        }
    }
}
