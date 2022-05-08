package com.cocofhu.ctb.kernel.util;

import java.io.*;

public interface CCloneable extends Serializable {

    default CCloneable cloneSelf() {
        CCloneable obj = null;
        try {
            //将对象序列化到流里
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(this);
            //将流反序列化成对象
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(is);
            obj = (CCloneable) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
