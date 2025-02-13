package StorageEngine;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import DataBase.MetaData.MetadataManager;
import DataBase.MetaData.TableData;
import DataBase.MetaData.TableMetaData;
import serialization.PageMapper;

public class StorageEngine {

    private static ByteBuffer streamBuffer ; 
    private String dbFilePAth = "src\\database.db";
    private static final int PAGE_SIZE = 4096 ;
    private static final int PAGE_TOTAL = 20;  
    private final byte[] byteStream  ; 
    private static Deque<Integer> pagesPool ; 
    public static MetadataManager metaDataManager ; 
    private static StorageEngine instance ; 


    private StorageEngine(){
        byteStream  = new byte[PAGE_SIZE*PAGE_TOTAL]; 
        streamBuffer = ByteBuffer.wrap(byteStream);
        StartUp();
        setupPagesPool();
    }


    public static void loadStorageEngine(){
       if(instance == null ){
            instance = new StorageEngine();
       }
    }

    private void setupPagesPool(){

        ArrayList<Integer> pagesPoolList = IntStream.rangeClosed(1, 31)
                                                    .boxed()
                                                    .collect(Collectors.toCollection(ArrayList::new));
                                                    
        List<Integer> pagesToRemove = metaDataManager.getReservedPages();
        List<Integer> AvailablePages = pagesPoolList.stream().filter( p -> !pagesToRemove.contains(p))
                                                             .collect(Collectors.toList());

        pagesPool = new ArrayDeque<>(AvailablePages);
    }

    private void StartUp(){
        metaDataManager = MetadataManager.fromBytes(getMetaDataPage());
    }

    public static TableMetaData fetchTableMetaData( String tableName ){
        return MetadataManager.getTableMetaData(tableName);
    }


    public static void addToMetaDataManager( TableMetaData tableMetaData ){
        metaDataManager.addTable(tableMetaData);
    }

    public static TableData readTable( TableData tableData ){
        TableMetaData table = tableData.getTableMetaData();
        for( Integer page : table.getPages() ){
            TableData tData = PageMapper.toTableData( tableData , getPage(page));
        }
        return tableData ; 
    }

    public static void insertRecord( String tableName , byte[] record ){
        TableMetaData table = fetchTableMetaData(tableName);
        int pageToWriteIn = getOffset(table);
        PageMapper.insertIntoPage(record , getPage(pageToWriteIn));
    }

    private static ByteBuffer getPage ( int pageN ){ 
        streamBuffer.position(pageN * PAGE_SIZE);
        streamBuffer.limit(pageN * PAGE_SIZE + PAGE_SIZE);
        return streamBuffer.slice();
    } 

    private static ByteBuffer getMetaDataPage(){
        return getPage(0);
    }

    private static int getOffset(TableMetaData table){
        int offset ; 
        try {
            offset = table.getLastPage();
            //check if page is empty
        } catch (Exception e) {
            offset = getNextPage();
            //in case new Page allocated assign new Page
            table.addPages(offset);
        }
        return offset ; 
    }

    private static int getNextPage(){ return pagesPool.pop() ; }

}