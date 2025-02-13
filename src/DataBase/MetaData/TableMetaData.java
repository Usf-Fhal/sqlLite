package DataBase.MetaData;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TableMetaData {
    
    
    private String tableName ; 
    private List<Column> columns  ;   
    private List<Integer> pages  ; 

    private TableMetaData(){
        columns = new ArrayList<>() ; 
        pages = new ArrayList<>() ; 
    }

    public TableMetaData( String table ){
        this();
        this.tableName = table ; 
    }

    public void updateColumnsList(List<String> columnsToKeep) {
        columns.removeIf(column -> !columnsToKeep.contains(column.getName()));
    }

    public void addColoum( Column colum ){
        columns.add(colum);
    }

    public void addPages( int page ){
        pages.add(page);
    }

    private void addPages( List<Integer> page ){
        pages.addAll(page);
    }
    
    public String getTableName(){
        return tableName ;
    }

    public static TableMetaData fromBytes( byte[] bytes ){
        return parsetDDL( new String(bytes , StandardCharsets.UTF_8)) ; 
    }

    private static TableMetaData parsetDDL(String compactedDDL) {
        Pattern COMPACT_DDL_PATTERN = Pattern.compile("\\d+(\\w+)\\((.*?)\\)\\((.*?)\\)");
        Matcher matcher = COMPACT_DDL_PATTERN.matcher(compactedDDL);

        if (!matcher.find()) {
            return null ;
        }

        String tableName = matcher.group(1).trim();
        String columnstring = matcher.group(2).trim();
        String pagesString = matcher.group(3).trim();

        TableMetaData tableMetaData = new TableMetaData(tableName);

        // Extract pages 
        if (!pagesString.isEmpty()) {
            List<Integer> pages = Arrays.stream(pagesString.split(","))
                                        .map(String::trim)
                                        .map(Integer::parseInt)
                                        .collect(Collectors.toList());
            tableMetaData.addPages(pages);
        }

        // Extract columns 
        if (!columnstring.isEmpty()) {
            Arrays.stream(columnstring.split(","))
                  .map(String::trim)
                  .map(col -> col.split(":"))
                  .filter(parts -> parts.length == 2)
                  .forEach(parts -> tableMetaData.addColoum(
                      new Column(parts[0].trim(), parts[1].trim())
                  ));
        }

        return tableMetaData;
    }
    
    private String getcolumnstring(){
        StringBuilder columBuilder = new StringBuilder() ; 
        for(Column column:columns){
            columBuilder.append(column).append(",");
        }
        return columBuilder.toString() ; 
    }

    private String getPageString(){
        StringBuilder pagesBuilder = new StringBuilder() ; 
        for(Integer p :pages){
            pagesBuilder.append(p).append(",");
        }
        return pagesBuilder.toString() ; 
    }


    public String getCompactDDL() {
        String DDL = String.format("%s(%s)", tableName, getcolumnstring());
        return DDL ;
    }


    public String getCompactPages() {
        String pagesList = String.format("(%s)",getPageString());
        return pagesList;
    }

    public List<Integer> getPages() {
        return pages;
    }
    
    public int getLastPage(){
        return pages.get(pages.size()-1);
    }

    public List<Column> getColumns() {
        return columns;
    }

}
