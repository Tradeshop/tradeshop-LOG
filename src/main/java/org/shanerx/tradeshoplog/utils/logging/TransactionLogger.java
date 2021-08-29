package org.shanerx.tradeshoplog.utils.logging;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.shanerx.tradeshop.framework.events.PlayerSuccessfulTradeEvent;
import org.shanerx.tradeshop.objects.Shop;
import org.shanerx.tradeshop.objects.ShopItemStack;
import org.shanerx.tradeshoplog.TradeShopLOG;
import org.shanerx.tradeshoplog.enumys.Setting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionLogger {

    private final TradeShopLOG tradeShopLOG;
    private LoggerOutputType loggerOutputType;
    private boolean newFile = false;
    private String localFormat;
    
    public TransactionLogger(TradeShopLOG tradeShopLOG) {
        this.tradeShopLOG = tradeShopLOG;

        try {
            loggerOutputType = LoggerOutputType.valueOf(Setting.OUTPUT_TYPE.getString());
        } catch (IllegalArgumentException | NullPointerException ex) {
            loggerOutputType = LoggerOutputType.TSV;
        }

        localFormat = Setting.TRANSACTION_LOG_FORMAT.getString().replaceAll("_@_", loggerOutputType.getDelimiter());
    }

    private PrintWriter buildWriter() {
        File file = new File(tradeShopLOG.getDataFolder(), "TransactionLogs" + File.separator + "TradeshopTransactionLog-" + getFileTimeString() + "." + loggerOutputType.getFileExtension());

        try {
            Files.createParentDirs(file);

            if (!file.exists()) {
                file.createNewFile();
                newFile = true;
            }

            FileWriter fileWriter = new FileWriter(file, true);
            return new PrintWriter(fileWriter);

        } catch (IOException ex)  {
            ex.printStackTrace();
        }

        return null;
    }

    public void logTransaction(PlayerSuccessfulTradeEvent event) {
        PrintWriter printWriter = buildWriter();
        if(printWriter != null) {
            Shop shop = event.getShop();

            if (newFile) {
                printWriter.println(localFormat.replaceAll("%", ""));
                newFile = false;
            }

            String owner = shop.getOwner().getName();

            if(owner == null)
                owner = "-Unknown-";

            printWriter.println(localFormat
                    .replaceAll("%Date", getTransactionDate())
                    .replaceAll("%Time", getTransactionTime())
                    .replaceAll("%ShopType", shop.getShopType().toString())
                    .replaceAll("%Owner", owner)
                    .replaceAll("%TradingPlayer", event.getPlayer().getName())
                    .replaceAll("%ShopLocation", shop.getShopLocationAsSL().toString())
                    .replaceAll("%World", shop.getShopLocationAsSL().getWorldName())
                    .replaceAll("%X", shop.getShopLocationAsSL().getX() + "")
                    .replaceAll("%Y", shop.getShopLocationAsSL().getY() + "")
                    .replaceAll("%Z", shop.getShopLocationAsSL().getZ() + "")
                    .replaceAll("%CostList", "\"" + getItemListAsString(event.getCost()) + "\"")
                    .replaceAll("%ProductList", "\"" + getItemListAsString(event.getProduct())) + "\"");
            printWriter.close();
        }
    }

    private String getItemListAsString(List<ShopItemStack> itemList) {
        final Gson gson = new GsonBuilder().create();
        JsonObject jsonObj = new JsonObject();
        for (int i = 0; i < itemList.size(); i++) {
            jsonObj.add(i + "", gson.toJsonTree(itemList.get(i).getItemStack()));

        }

        return gson.toJson(jsonObj);
    }

    private String getFileTimeString() {
        String dateFormat = "yyyy";
        switch(Setting.LOG_TIME_SEPARATION.getString()) {
            case "M":
                dateFormat += "-MM";
                break;
            case "d":
                dateFormat += "-MM-dd";
                break;
            case "m":
                dateFormat += "-MM-dd'T'HH-mm";
                break;
            case "s":
                dateFormat += "-MM-dd'T'HH-mm-ss";
                break;
            default:
            case "H":
                dateFormat += "-MM-dd'T'HH";
                break;
        }

        return new SimpleDateFormat(dateFormat).format(new Date());
    }

    private String getTransactionTime() {
        return new SimpleDateFormat("HH-mm-ss").format(new Date());
    }

    private  String getTransactionDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

}
