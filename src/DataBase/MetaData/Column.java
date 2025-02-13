package DataBase.MetaData;

import java.util.Arrays;

import Exceptions.ColumnNotSupported;

public class Column {
    
    private String name ; 
    private DataType type ;

    public Column(String name, String type) {
        try {
            this.name = name.toUpperCase();
            this.type = DataType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ColumnNotSupported(type + " is unsupported. Supported Types are: " + Arrays.toString(DataType.values()));
        }
    }

    @Override
    public String toString() {
        return  name.toUpperCase() + ":" + type.toString().toUpperCase() ;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    } 
    
}
