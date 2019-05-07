package com.weikwer.market.Utils;

import java.util.ArrayList;

public class PrintVue {
    String[] inputs;
    String[] buttons;
    String[] methods;
    int[] types;
    String title;
    String var="myvarRAND";
    int T=0;
    static public void main(String[] args){
        String[] inputs={"用户手机号","用户密码","性别","生日","修改"};
        String[] methods={"en1","en2"};
        int[] types={0,0,0,0,2};

        PrintVue p=new PrintVue("编辑用户").setInputs(inputs).setMethods(methods).setTypes(types);
        System.out.print(p.get());

    }

    public PrintVue(String title){
        this.title=title;
    }

    public String get(){
        ArrayList<String> inputsVar=new ArrayList<String>();
        String firstLine="<template>\n" +
                "    <div>\n" +
                "        <p>"+title+"</p>\n";
        String secondLine="";
        for(int i=0;i<inputs.length;i++){
            if(types[i]==0) {
                secondLine+="        <el-input v-model=\"input"+var+T+"\" placeholder=\""+inputs[i]+"\"></el-input>\n";
                inputsVar.add(i,"input"+var+T);
                ++T;
            }
            else if(types[i]==1){
                secondLine+="        <el-input\n" +
                        "          type=\"textarea\"\n" +
                        "          :rows=\"2\"\n" +
                        "          placeholder=\""+inputs[i]+"\"\n" +
                        "          v-model=\"input"+var+T+"\">\n";
                inputsVar.add(i,"input"+var+T);
                ++T;
            }else if(types[i]==2){
                secondLine+="        <button v-on:click=\"btn"+var+T+"\">"+inputs[i]+"</button>\n";
                inputsVar.add(i,"btn"+var+T);
                ++T;
            }

        }
        String interL="";
        for(int i=0;i<inputsVar.size();i++){
            if(types[i]!=2)
            interL+="            "+inputsVar.get(i)+":'',\n";
        }


        String thirdLine="    </div>\n" +
                "</template>\n" +
                "\n" +
                "<script>\n" +
                "import axios from 'axios'\n" +
                "export default {\n" +
                "    data(){\n" +
                "        return {\n" +interL+
                "        }\n" +
                "    },\n" +
                "    methods:{\n";
        String methodLine="";


        //函数定义
        for(int i=0;i<inputsVar.size();i++){
            if(types[i]==2)
                methodLine+="        "+inputsVar.get(i)+"(){\n" +
                        "            var self = this\n" +
                        "            axios.post(\"/api/order/add\",{\n" +
                        "                var:self.var,\n" +

                        "            }).then(function (response) {\n" +
                        "\n" +
                        "            }).catch(function (error) {\n" +
                        "                \n" +
                        "            })\n" +
                        "        }";
        }
        String endLine="    }\n" +
                "}\n" +
                "</script>\n";

        return firstLine+secondLine+thirdLine+methodLine+endLine;



    }

    public String[] getInputs() {
        return inputs;
    }

    public PrintVue setInputs(String[] inputs) {
        this.inputs = inputs;
        return this;
    }

    public String[] getMethods() {
        return methods;
    }

    public PrintVue setMethods(String[] methods) {
        this.methods = methods;
        return this;
    }

    public int[] getTypes() {
        return types;
    }

    public PrintVue setTypes(int[] types) {
        this.types = types;
        return this;
    }
}
