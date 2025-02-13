package serialization;

public class MetaDataMapper{

    private static final int HEADER_SIZE = 12;  // checksum (4) + offset (4) + nexPage(4)
    private static final int PAGE_SIZE = 4096;
    private int checksum ; //TODO
    private int offset ; // TODO  if offset pointing to the currentPage still has free slot else return offset for the nextAvaible Page
    private int nextPage ; //TODO
    private byte[] metaData ; //stored in form length(ddl)(pages)

    private MetaDataMapper(){
        metaData = new byte[PAGE_SIZE-HEADER_SIZE];
        offset = HEADER_SIZE ;
    }

    // TODO
    private void writeIntoMetaData( byte[] metaDataBytes ){
        System.arraycopy(metaDataBytes, 0, this.metaData, this.offset, metaDataBytes.length);
    }

}
