package ExecutionEngine.Plans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DataBase.MetaData.TableMetaData;
import StorageEngine.StorageEngine;
import DataBase.MetaData.Column ; 

public class CreatePlan implements QueryPlan {

    //VALID SYTAX FOR CREATE TABLE : CREATE TABLE USER ( ID : INTGER , NAME : STRING )
    final Pattern CREATE_TABLE_PATTERN = Pattern.compile("CREATE TABLE (\\w+) \\((.*?)\\)");

    @Override
    public void execute( String query) {
        TableMetaData tableMetaData = toTableMetaData(query);
        StorageEngine.addToMetaDataManager(tableMetaData);
    }
    
    private TableMetaData toTableMetaData( String query ){
        TableMetaData tableMetaData = null ; 
        
        Matcher matcher = CREATE_TABLE_PATTERN.matcher(query);

        if (matcher.find()) {
            String tableName = matcher.group(1); // EXTARCT TABLENAME
            String columnsDef = matcher.group(2); // EXTARCT COLOUMS

            tableMetaData = new TableMetaData(tableName);

            String[] columnsArray = columnsDef.split(",");
            for (String col : columnsArray) {
                String[] parts = col.trim().split(":");
                if (parts.length == 2) {
                    String columnName = parts[0].trim();
                    String columnType = parts[1].trim();
                    tableMetaData.addColoum( new Column(columnName, columnType) );
                }
            }
        }
        return tableMetaData ; 
    }
}