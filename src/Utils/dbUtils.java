package Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import DataBase.MetaData.Column;
import DataBase.MetaData.TableData;

//BY CHATGPT
public class dbUtils {
    
    private static final int COLUMN_WIDTH = 15;
    private static List<Integer> columnsWidth ;

    public static void tableDrawer( TableData tableData ){
        List<String> colums = tableData.getTableMetaData().getColumns().stream().map(Column::getName).toList();
        List<List<String>> rows = tableData.getRows();
        initilaizeColomnsWith(colums);

        printSeparator();
        printRow(colums);
        printSeparator();
        for(List<String> row : rows ){
            printRow(row);
        }
        printSeparator();

    }

    private static void initilaizeColomnsWith(List<String> columns) {
        columnsWidth = new ArrayList<>(Collections.nCopies(columns.size(), COLUMN_WIDTH));
    }
    
    private static void printSeparator() {
        for (Integer width : columnsWidth) {
            System.out.print("+");
            System.out.print("-".repeat(width + 2));  
        }
        System.out.println("+");
    }

    private static void printRow(List<String> rows) {
        for (int i = 0; i < columnsWidth.size(); i++) {
            System.out.print("| ");
            String cell = (i < rows.size()) ? rows.get(i) : "";  
            System.out.print(String.format("%-" + columnsWidth.get(i) + "s", cell)); 
            System.out.print(" ");
        }
        System.out.println("|");
    }

}
