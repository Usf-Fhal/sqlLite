package ExecutionEngine.Plans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import DataBase.MetaData.Column;
import DataBase.MetaData.TableData;
import DataBase.MetaData.TableMetaData;
import StorageEngine.StorageEngine;
import Utils.dbUtils;

public class SelectPlan implements QueryPlan {

    private TableMetaData tableMetaData;
    private final Pattern SELECT_QUERY_PATTERN = Pattern.compile("SELECT\\s+(.+?)\\s+FROM\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
    TableData selectedTabData ;
    
    @Override
    public void execute( String query ) {
        Matcher matcher = SELECT_QUERY_PATTERN.matcher(query);
        List<String> columns = new ArrayList<>();
        String tableName = null ; 

        if (matcher.find()) {
            String columnsPart = matcher.group(1).trim();
            tableName = matcher.group(2).trim();

            if (columnsPart.equals("*")) {
                tableMetaData = StorageEngine.fetchTableMetaData(tableName);
                columns = tableMetaData.getColumns().stream().map(Column::getName).collect(Collectors.toList());
            } else {
                //update coloums list 
                columns = Arrays.asList(columnsPart.split("\\s*,\\s*"));
                tableMetaData.updateColumnsList(columns);
            }
            
            selectedTabData = new TableData(tableName);
            selectedTabData.setTableMetaData(tableMetaData);
        }
        StorageEngine.readTable(selectedTabData);
        dbUtils.tableDrawer(selectedTabData);
    }

    
}
