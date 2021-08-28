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

package org.shanerx.tradeshoplog;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.shanerx.tradeshop.utils.Updater;
import org.shanerx.tradeshoplog.enumys.Setting;
import org.shanerx.tradeshoplog.listeners.*;
import org.shanerx.tradeshoplog.utils.logging.TransactionLogger;

public class TradeShopLOG extends JavaPlugin {

	private TransactionLogger transactionLogger;

	private final int bStatsPluginID = 12596;
	private Metrics metrics;

	@Override
	public void onEnable() {

		Setting.reload();

		transactionLogger = new TransactionLogger(this);

		PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new SuccessfulTradeEventListener(this), this);
		pm.registerEvents(new TradeShopReloadEventListener(), this);

		if (Setting.CHECK_UPDATES.getBoolean()) {
			new Thread(() -> getUpdater().checkCurrentVersion()).start();
		}

		if (Setting.ALLOW_METRICS.getBoolean()) {
			metrics = new Metrics(this, bStatsPluginID);
			getLogger().info("Metrics successfully initialized!");

		} else {
			getLogger().warning("Metrics are disabled! Please consider enabling them to support the authors!");
		}

	}

	public Updater getUpdater() {
		return new Updater(getDescription(), "https://raw.githubusercontent.com/Tradeshop/tradeshop-LOG/master/version.txt", "https://github.com/Tradeshop/tradeshop-LOG/releases");
	}

	public TransactionLogger getTransactionLogger() {
		return transactionLogger;
	}
}
