package org.thanatos.flowgeek.convert;

import android.util.Log;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.transform.Transform;

/**
 * Created by thanatos on 16/1/31.
 */
public class IntegerConvert implements Converter<Integer> {

    @Override
    public Integer read(InputNode node) throws Exception {
        try {
            String value = node.getValue();
            return Integer.valueOf(node.getValue());
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public void write(OutputNode node, Integer value) throws Exception {

    }
}
