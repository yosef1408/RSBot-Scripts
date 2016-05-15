package is0lates.GrandExchangeAlcher.model;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class AlchItemTableModel extends AbstractTableModel {

    public static final int COLUMN_NO = 0;
    public static final int COLUMN_ID = 1;
    public static final int COLUMN_NAME = 2;
    public static final int COLUMN_LIMIT = 3;
    public static final int COLUMN_ALCH_PRICE = 4;
    public static final int COLUMN_GUIDE_PRICE = 5;
    public static final int COLUMN_GUIDE_RROFIT = 6;
    public static final int COLUMN_GUIDE_MAX_PROFIT = 7;
    public static final int COLUMN_MEMBERS = 8;
    public static final int COLUMN_BUY_PRICE = 9;
    public static final int COLUMN_CALC_PROFIT = 10;
    public static final int COLUMN_CALC_MAX_PROFIT = 11;

    private static int natureRunePrice;

    private String[] columnNames = {"#", "ID", "Name", "Limit", "Alch Price", "Guide Price", "Guide Profit", "Guide Max Profit", "Members", "Buy Price", "Calc Profit", "Calc Max Profit"};
    public List<AlchItem> alchItemList;

    public AlchItemTableModel(List<AlchItem> alchItemList, int natureRunePrice) {
        this.alchItemList = alchItemList;
        natureRunePrice = natureRunePrice;
        int indexCount = 1;
        for (AlchItem alchItem : alchItemList) {
            alchItem.index = (indexCount++);
        }
    }


    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return alchItemList.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (alchItemList.isEmpty()) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int column) { // custom isCellEditable function
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AlchItem alchItem = alchItemList.get(rowIndex);
        Object returnValue = null;

        switch (columnIndex) {
            case COLUMN_NO:
                returnValue = alchItem.index;
                break;
            case COLUMN_ID:
                returnValue = alchItem.id;
                break;
            case COLUMN_NAME:
                returnValue = alchItem.name;
                break;
            case COLUMN_LIMIT:
                returnValue = alchItem.limit;
                break;
            case COLUMN_ALCH_PRICE:
                returnValue = alchItem.alchPrice;
                break;
            case COLUMN_GUIDE_PRICE:
                returnValue = alchItem.price;
                break;
            case COLUMN_GUIDE_RROFIT:
                returnValue = alchItem.profit;
                break;
            case COLUMN_GUIDE_MAX_PROFIT:
                returnValue = alchItem.maxProfit;
                break;
            case COLUMN_MEMBERS:
                returnValue = alchItem.members;
                break;
            case COLUMN_BUY_PRICE:
                returnValue = alchItem.buyPrice;
                break;
            case COLUMN_CALC_PROFIT:
                returnValue = alchItem.calcProfit;
                break;
            case COLUMN_CALC_MAX_PROFIT:
                returnValue = alchItem.calcMaxProfit;
                break;
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
        return returnValue;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        AlchItem alchItem = alchItemList.get(rowIndex);
        if (columnIndex == COLUMN_NO) {
            alchItemList.get(rowIndex).index = Integer.parseInt(value.toString());
        }
        if (columnIndex == COLUMN_BUY_PRICE) {
            alchItemList.get(rowIndex).buyPrice = Integer.parseInt(value.toString());
            alchItemList.get(rowIndex).calcMaxProfit(natureRunePrice);
        }
    }

}
