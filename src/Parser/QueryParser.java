package Parser;

import Exceptions.QueryTypeUnsupported;
import ExecutionEngine.Plans.CreatePlan;
import ExecutionEngine.Plans.InsertPlan;
import ExecutionEngine.Plans.SelectPlan;

public class QueryParser {

    static final String INSERT_PREFIX ="INSERT"; 
    static final String DELETE_PREFIX ="DELEET"; 
    static final String CREATE_PREFIX ="CREATE"; 
    static final String SELECT_PREFIX ="SELECT"; 
    
    public static void parseQuery( String query ){ 

        query = query.trim().replaceAll("\\s+", " ");

        if(query.startsWith(INSERT_PREFIX)){
            new InsertPlan().execute(query);
        }else if(query.startsWith(CREATE_PREFIX)){
            new CreatePlan().execute(query);
        }else if(query.startsWith(SELECT_PREFIX)){
            new SelectPlan().execute(query);
        }else{
            throw new QueryTypeUnsupported("Query Type is Unsupported for the moment! " + query);
        }
    }   


}