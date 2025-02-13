package DataBase.MetaData;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//Singleton PATTERN
public class MetadataManager {
    
    private static final int HEADER_SIZE = 12;  // checksum (4) + offset (4) + nexPage(4)
    private static final int PAGE_SIZE = 4096;
    private static ArrayList<TableMetaData> tables ; 
    private static MetadataManager  instance ; 

    public static MetadataManager getInstanceOfMetadataManager(){
        if(instance==null){
            instance = new MetadataManager();
        }
        return instance ; 
    }

    private MetadataManager(){
        tables = new ArrayList<>();
    }

    public void addTable( TableMetaData table ){
        tables.add(table);
    }

    //need better naming
    public static TableMetaData getTableMetaData(String tableName) {
        return tables.stream()
                .filter(table -> table.getTableName().equalsIgnoreCase(tableName))
                .findFirst()
                .orElse(null);
    }
    
    //need better naming 
    public ArrayList<Integer> getReservedPages (){
        ArrayList<Integer> reservedPages  = new ArrayList<>();
        if(!reservedPages .isEmpty()){
            tables.stream()
            .flatMap(tMetadata -> tMetadata.getPages().stream())
            .forEach(reservedPages ::add);
        }
        return reservedPages  ; 
    }
    
    //TODO
    public static byte[] toBytes(){
        ByteBuffer buffer = ByteBuffer.allocate(PAGE_SIZE);
        //allocate Hedaer TODO LATER
        buffer.position(HEADER_SIZE);
        for(TableMetaData tMetadata : tables ){
            int totalLength = tMetadata.getCompactDDL().length() + tMetadata.getCompactPages().length() ; 
            buffer.putInt(totalLength);
            buffer.put(tMetadata.getCompactDDL().getBytes(StandardCharsets.UTF_8));
            buffer.put(tMetadata.getCompactPages().getBytes(StandardCharsets.UTF_8));
        }
        return buffer.array();
    }

    public static MetadataManager fromBytes(ByteBuffer buffer) {
        MetadataManager metadataManager = getInstanceOfMetadataManager();

        buffer.position(HEADER_SIZE);

        while (buffer.hasRemaining()) {
            
            int ddlLength = buffer.getInt();
            //break in case page is empty
            if(ddlLength==0){
                break;
            }

            byte[] ddlBytes = new byte[ddlLength];
            buffer.get(ddlBytes);
            
            TableMetaData tableMetaData = TableMetaData.fromBytes(ddlBytes);
            metadataManager.addTable(tableMetaData);
        }

        return metadataManager;
    }


}
