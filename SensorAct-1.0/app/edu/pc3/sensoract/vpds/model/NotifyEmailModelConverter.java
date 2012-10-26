/**
 * 
 */
package edu.pc3.sensoract.vpds.model;

import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.MappingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * @author samy
 *
 */
public class NotifyEmailModelConverter extends TypeConverter {

	public NotifyEmailModelConverter() {
		super(NotifyEmailModel.class);
	}
	
    @Override
    public Object decode(Class targetClass, Object fromDBObject, MappedField optionalExtraInfo) throws MappingException {
            if (fromDBObject == null) return null;
            //IntegerConverter intConv = new IntegerConverter();
            //Integer i = (Integer)intConv.decode(targetClass, fromDBObject, optionalExtraInfo);
            System.out.println("inside EmailEntityConverter decode...");
            Integer i = 10;
            return new Character((char)i.intValue());
    }
    
    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
    	System.out.println("inside EmailEntityConverter encode... " + value);
    	
    	DBObject dbo = new BasicDBObject();
    	dbo.put("key1", "value1");
    	
            Character c = (Character)value;
            return (int)c.charValue();
            
            //return dbo;
    }
   
	
//	@Override
//	public Object decode(Class arg0, Object arg1, MappedField arg2)
//			throws MappingException {
//		System.out.println("inside decode...");
//		// TODO Auto-generated method stub
//		return arg1;
//	}

}
