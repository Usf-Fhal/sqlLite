package serialization;

import java.nio.ByteBuffer;
import java.util.BitSet;
import DataBase.MetaData.TableData;

public class PageMapper {
    private static final int HEADER_SIZE = 30 ;  // checksum (4) + offset (4) + count(4) + pageID(4) + bitmp(14)
    private static final int PAGE_SIZE = 4096;
    private int checksum; //TODO
    private int pageID ; //TODO
    private int offset ; // TODO  if offset pointing to the currentPage still has free slot else return offset for the nextAvaible Page
    private int count;         //TODO   
    private BitSet bitmap;   //for begining i suppsoe page can hold at max 112 page     
    private byte[] rows ; 

    //currently Support write in same page
    public static void insertIntoPage( byte[] recordByte , ByteBuffer buffer){
        int checksum = buffer.getInt();
        int pageID = buffer.getInt(); 
        int offset = buffer.getInt()  ; 
        int count= buffer.getInt();  
        byte[] bitmapBytes = new byte[14];
        buffer.get(bitmapBytes);

        buffer.position(offset+ HEADER_SIZE);
        buffer.putInt(recordByte.length); // put the length of the record ahead of it 
        buffer.put(recordByte);

        offset+=recordByte.length + 4 ; // (4) byte for record Length
        count+=1;

        buffer.putInt( 8 , offset); // 8 is the index offset
        buffer.putInt( 12 , count); // 12 is the index count
    }
    
    //need better structure and better naming
    public static TableData toTableData( TableData tableData , ByteBuffer buffer) {

        buffer.position(8);  // Skip checksum (4 bytes) and pageID (4 bytes)

        int offset = buffer.getInt(); // Read offset at position 8
    
        // Read the actual row data into a byte array
        byte[] rows = new byte[offset];
        buffer.position(HEADER_SIZE);
        buffer.get(rows); // Read data directly into row

        //need better naming
        return  TableData.fromBytes(tableData, rows) ;
    }
}
