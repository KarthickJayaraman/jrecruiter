/*
*	http://www.jrecruiter.org
*
*	Disclaimer of Warranty.
*
*	Unless required by applicable law or agreed to in writing, Licensor provides
*	the Work (and each Contributor provides its Contributions) on an "AS IS" BASIS,
*	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied,
*	including, without limitation, any warranties or conditions of TITLE,
*	NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are
*	solely responsible for determining the appropriateness of using or
*	redistributing the Work and assume any risks associated with Your exercise of
*	permissions under this License.
*
*/
package org.jrecruiter.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
* From Appfuse
* @author Gunnar Hillert
* @version $Id:JobService.java 128 2007-07-27 03:55:54Z ghillert $
*/
public class DateConverter extends StrutsTypeConverter {
    public static final String format = "MM/dd/yyyy";

    public Object convertFromString(Map ctx, String[] value, Class arg2) {
        if (value[0] == null || value[0].trim().equals("")) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(value[0]);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public String convertToString(Map ctx, Object data) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(data);
    }
}