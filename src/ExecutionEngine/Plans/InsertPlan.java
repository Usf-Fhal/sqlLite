package ExecutionEngine.Plans;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import DataBase.MetaData.Column;
import DataBase.MetaData.DataType;
import DataBase.MetaData.TableMetaData;
import Exceptions.ColoumNotExist;
import Exceptions.InvalidColoumValue;
import StorageEngine.StorageEngine;

public class InsertPlan implements QueryPlan {

    private TableMetaData tableMetaData;
    private final Pattern INSERT_INTO_TABLE_LONG_PATTERN = Pattern.compile("INSERT INTO TABLE\\s+(\\w+)\\s*\\(([^)]+)\\)\\s*VALUES\\s*\\(([^)]+)\\)");
    private final Pattern INSERT_INTO_TABLE_SHORT_PATTERN = Pattern.compile("INSERT INTO\\s+(\\w+)\\s*VALUES\\s*\\(([^)]+)\\)"); // Handles short form: INSERT INTO USER VALUES (25, 'Youssef')

    @Override
    public void execute(String query) {
        Matcher matcher = INSERT_INTO_TABLE_LONG_PATTERN.matcher(query);
        String valuesPart , tableName , columnsPart ; 

        if (matcher.find()) {
            // Extracting table name, columns, and values
            tableName = matcher.group(1).trim();
            columnsPart = matcher.group(2).trim();
            valuesPart = matcher.group(3).trim();

            // Fetch MetaData
            tableMetaData = StorageEngine.fetchTableMetaData(tableName);

            // Columns list
            List<String> columns = Arrays.asList(columnsPart.split("\\s*,\\s*"));

            // Check columns existence
            checkColumnsExistence(tableMetaData, columns);

            // Validate types of arguments
            checkIfArgumentValid(valuesPart, columns);
        } else {
            Matcher shortMatcher = INSERT_INTO_TABLE_SHORT_PATTERN.matcher(query);
            if (shortMatcher.find()) {
                tableName = shortMatcher.group(1).trim();
                valuesPart = shortMatcher.group(2).trim();

                // Fetch MetaData
                tableMetaData = StorageEngine.fetchTableMetaData(tableName);

                // Fetch all columns from the table
                List<String> columns = tableMetaData.getColumns().stream().map(Column::getName).collect(Collectors.toList());

                // Validate types of arguments
                checkIfArgumentValid(valuesPart, columns);
            } else {
                throw new IllegalArgumentException("Invalid INSERT INTO query format.");
            }
        }
        //insert Record
        StorageEngine.insertRecord(tableName,valuesPart.getBytes());
    }

    private void checkColumnsExistence(TableMetaData tableMetaData, List<String> columns) {
        List<String> columnsInTable = tableMetaData.getColumns().stream().map(Column::getName).collect(Collectors.toList());
        for (String col : columns) {
            if (!columnsInTable.contains(col)) {
                String msg = "NO COLUMN " + col + " DEFINED IN THE TABLE " + tableMetaData.getTableName();
                throw new ColoumNotExist(msg);
            }
        }
    }

    private void checkIfArgumentValid(String valuesPart, List<String> columns) {
        List<String> values = Arrays.asList(valuesPart.split("\\s*,\\s*"));
        List<DataType> columnTypes = getColumnTypes(columns);

        if (values.size() != columnTypes.size()) {
            throw new IllegalArgumentException("Number of values does not match number of columns.");
        }
        //check coloums Existence
        checkColumnsExistence(tableMetaData , columns);
        for (int i = 0; i < values.size(); i++) {
            validateValue(values.get(i).trim(), columnTypes.get(i), columns.get(i));
        }
    }

    private List<DataType> getColumnTypes(List<String> columns) {
        return tableMetaData.getColumns().stream()
                .filter(col -> columns.contains(col.getName()))
                .map(Column::getType)
                .collect(Collectors.toList());
    }

    //CHATGPT GENERATED
    private void validateValue(String value, DataType type, String columnName) {
        switch (type) {
            case INT:
                if (!value.matches("-?\\d+")) {
                    throw new InvalidColoumValue("Invalid INTEGER value for column " + columnName + ": " + value);
                }
                break;
            case STRING:
                if (!value.matches("'.*'") && !value.matches("\".*\"")) {
                    throw new InvalidColoumValue("Invalid STRING value for column " + columnName + ": " + value);
                }
                break;
            case BOOLEAN:
                if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                    throw new InvalidColoumValue("Invalid BOOLEAN value for column " + columnName + ": " + value);
                }
                break;
            case DATE:
                if (!value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    throw new InvalidColoumValue("Invalid DATE value for column " + columnName + ": " + value);
                }
                break;
            default:
                throw new InvalidColoumValue("Unsupported data type for column " + columnName);
        }
    }
}
