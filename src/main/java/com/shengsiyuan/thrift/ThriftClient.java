/*
 * Copyright (C) 2009-2018 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.shengsiyuan.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import thrift.generated.Person;
import thrift.generated.PersonService;

/**
 * ThriftClient
 *
 * @author huaishi
 * @since 2019-03-04
 */

public class ThriftClient {

    public static void main(String[] args) {
        TTransport transport = new TFramedTransport(new TSocket("localhost",8899),600);
        TProtocol protocol = new TCompactProtocol(transport);

        PersonService.Client client = new PersonService.Client(protocol);

        try{
            transport.open();
            Person person = client.getPersonByUsername("张三");
            System.out.println(person);

            Person person2 = new Person();
            person2.setAge(30);
            person2.setMarried(true);
            person2.setUsername("李四");
            client.savePerson(person2);


        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage(), ex);
        }finally {
            transport.close();
        }
    }

}