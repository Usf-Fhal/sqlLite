package DataBase.MetaData;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TableData {

    private String tableName;
    private TableMetaData tableMetaData ; 
    private List<List<String>> rows;
    private int tableCount ; 

    public TableData(String tableName) {
        this.tableName = tableName;
        this.rows = new ArrayList<>();
    }

    public void addTableCount( int count ){
        this.tableCount+=count ; 
    }

    public void addRows(List<List<String>> row) {
        this.rows.addAll(rows);
    } 

    public void addRow(List<String> row) {
        this.rows.add(row);
    }

    //need better naming & structure
    public static TableData  fromBytes(TableData tableData, byte[] rowBytes) {
        ByteBuffer buffer = ByteBuffer.wrap(rowBytes);

        while (buffer.hasRemaining()) {
            int recordLength = buffer.getInt();

            // Ensure the byte array matches the record length
            if (buffer.remaining() < recordLength) {
                throw new IllegalArgumentException("Corrupted data: Record length exceeds buffer remaining.");
            }

            byte[] recordBytes = new byte[recordLength];
            buffer.get(recordBytes);

            List<String> row = byteToList(recordBytes);
            tableData.addRow(row);
        }

        return tableData;
    }

    private static List<String> byteToList(byte[] recordBytes) {
        String recordString = new String(recordBytes, StandardCharsets.UTF_8).trim();

        int start = recordString.startsWith("(") ? 1 : 0;
        int end = recordString.endsWith(")") ? recordString.length() - 1 : recordString.length();
        recordString = recordString.substring(start, end);

        String[] elements = recordString.split(",");

        return List.of(elements);
    }
    public String getTableName() {
        return tableName;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public int getTableCount() {
        return tableCount;
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

    public void setTableMetaData(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

}
