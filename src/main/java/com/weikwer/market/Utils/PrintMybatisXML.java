package com.weikwer.market.Utils;

import com.weikwer.market.bean.Address;

import java.lang.reflect.Field;

/**
 *
 * @param <T>
 */
public class PrintMybatisXML<T> {
    private String out="";
    private T t;
    private String tableName;
    private String className;
    private Field[] fields;
    private String pk;
    private boolean hasPK=false;
    private String[] insertByFields;

    private String[] updateByFields;

    String[] delByFields=null;
    static public void main(String[] args){
        String[] delbyFieldsp={"goodsId"};
        String[] iby={"addrId"};
        PrintMybatisXML p=new PrintMybatisXML<Address>(new Address());

        System.out.print(p.select()+p.selectCount());
        //System.out.print(new Date().toString());
    }

    public PrintMybatisXML(T t){
        init(t);
    }

    public PrintMybatisXML(T t, String pk){
        init(t);
        this.pk=pk;
        hasPK=true;
    }

    public PrintMybatisXML setPrimaryKey(String pk){
        this.pk=pk;
        hasPK=true;
        return this;
    }

    public PrintMybatisXML setbyFields(String[] inputByFields){
        this.insertByFields =inputByFields;
        return this;
    }

    public PrintMybatisXML setDelByFields(String[] inputByFields){
        this.delByFields=inputByFields;
        return this;
    }

    public PrintMybatisXML setDelUpdateByFields(String[] inputByFields){
        this.updateByFields=inputByFields;
        return this;
    }

    private void init(T t){
        this.t=t;
        className =t.getClass().getName();
        String[] TNameArray=className.split("\\.");
        String TName=TNameArray[TNameArray.length-1];
        tableName=TName.substring(0,1).toLowerCase()+TName.substring(1,TName.length());
        fields = t.getClass().getDeclaredFields();
    }


    private String update(){
        String firstLine="  <update id=\"update\" parameterType=\""+className+"\">\n";
        String secondLine="    update "+tableName+"\n";
        String setLine="    <set>\n      <trim prefix=\"\" suffix=\"\" suffixOverrides=\",\" >\n";
        for(Field fd:fields){
            String fieldName=fd.getName();
            StringBuilder tableFieldName=new StringBuilder();
            for(int i=0;i<fieldName.length();i++){
                if(fieldName.charAt(i)<='Z'&&fieldName.charAt(i)>='A'){
                    tableFieldName.append("_"+fieldName.substring(i,i+1).toLowerCase());
                }else {
                    tableFieldName.append(fieldName.charAt(i));
                }
            }
            String setLineUnit="      <if test=\""+fieldName+" != null\" >\n" +
                    "        "+tableFieldName.toString()+" = #{"+fieldName+"} ,\n" +
                    "      </if>\n";
            setLine+=setLineUnit;
        }
        setLine+="      </trim>\n    </set>\n";
        String whereLine="    where\n";

        whereLine+="    <trim prefix=\"\" suffix=\"\" suffixOverrides=\"and\" >\n";
        for(Field fd:fields){
            String fieldName=fd.getName();
            StringBuilder tableFieldName=new StringBuilder();
            for(int i=0;i<fieldName.length();i++){
                if(fieldName.charAt(i)<='Z'&&fieldName.charAt(i)>='A'){
                    tableFieldName.append("_"+fieldName.substring(i,i+1).toLowerCase());
                }else {
                    tableFieldName.append(fieldName.charAt(i));
                }
            }
            String whereLineUnit="      <if test=\""+fieldName+" != null\" >\n" +
                    "        "+tableFieldName.toString()+" = #{"+fieldName+"} and\n" +
                    "      </if>\n";
            whereLine+=whereLineUnit;
        }
        whereLine+="    </trim>\n";
        String endLine="  </update>";
        return firstLine+secondLine+setLine+whereLine+endLine;
    }




    private String updateBy(){
        String firstLine="  <update id=\"update\" parameterType=\""+className+"\">\n";
        String secondLine="    update "+tableName+"\n";
        String setLine="    <set>\n      <trim prefix=\"\" suffix=\"\" suffixOverrides=\",\" >\n";
        for(Field fd:fields){
            String fieldName=fd.getName();
            StringBuilder tableFieldName=new StringBuilder();
            for(int i=0;i<fieldName.length();i++){
                if(fieldName.charAt(i)<='Z'&&fieldName.charAt(i)>='A'){
                    tableFieldName.append("_"+fieldName.substring(i,i+1).toLowerCase());
                }else {
                    tableFieldName.append(fieldName.charAt(i));
                }
            }
            String setLineUnit="      <if test=\""+fieldName+" != null\" >\n" +
                    "        "+tableFieldName.toString()+" = #{"+fieldName+"} ,\n" +
                    "      </if>\n";
            setLine+=setLineUnit;
        }
        setLine+="      </trim>\n    </set>\n";
        String whereLine="    where\n";

        whereLine+="    <trim prefix=\"\" suffix=\"\" suffixOverrides=\"and\" >\n";
        for(String fd:updateByFields){

            String sqlFieldName=toSqlField(fd);

            String whereLineUnit= "        "+sqlFieldName+" = #{"+fd+"} and\n" ;
            whereLine+=whereLineUnit;
        }
        whereLine+="    </trim>\n";
        String endLine="  </update>";
        return firstLine+secondLine+setLine+whereLine+endLine;
    }

    private String selectCount(){
        String firstLine="  <select id=\"selectCount\" parameterType=\"java.util.Map\" resultType=\"java.lang.Integer\">\n";
        String secondLine="    select count(*) from "+tableName+ifAllNull()+" where\n";
        String lastLine="  </select>\n";

        String centerLine="    <trim prefix=\"\" suffix=\"\" suffixOverrides=\"and\" >\n";
        for(Field fd:fields){
            String fieldName=fd.getName();
            StringBuilder tableFieldName=new StringBuilder();
            for(int i=0;i<fieldName.length();i++){
                if(fieldName.charAt(i)<='Z'&&fieldName.charAt(i)>='A'){
                    tableFieldName.append("_"+fieldName.substring(i,i+1).toLowerCase());
                }else {
                    tableFieldName.append(fieldName.charAt(i));
                }
            }
            String centerLineUnit="      <if test=\""+fieldName+" != null\" >\n" +
                    "        "+tableFieldName.toString()+" = #{"+fieldName+"} and\n" +
                    "      </if>\n";
            centerLine+=centerLineUnit;
        }
        centerLine+="    </trim>\n  </if>\n";

        return firstLine+secondLine+centerLine+lastLine;
    }

    private String select(){

        String parameterType="\"java.util.Map\"";
        String firstLine="  <select id=\"select\" parameterType="+
                         parameterType+
                         " resultMap=\"BaseResultMap\">\n";

        String secondLine="    select * from "+tableName+ifAllNull()+" where\n";
        String lastLine="  </select>\n";
        String centerLine="    <trim prefix=\"\" suffix=\"\" suffixOverrides=\"and\" >\n";
        for(Field fd:fields){
            String fieldName=fd.getName();
            StringBuilder tableFieldName=new StringBuilder();
            for(int i=0;i<fieldName.length();i++){
                if(fieldName.charAt(i)<='Z'&&fieldName.charAt(i)>='A'){
                    tableFieldName.append("_"+fieldName.substring(i,i+1).toLowerCase());
                }else {
                    tableFieldName.append(fieldName.charAt(i));
                }
            }
            String centerLineUnit="      <if test=\""+fieldName+" != null\" >\n" +
                    "        "+tableFieldName.toString()+" = #{"+fieldName+"} and\n" +
                    "      </if>\n";
            centerLine+=centerLineUnit;
        }

        centerLine+="    </trim>\n  </if>\n";
        centerLine+="    limit #{begin},#{pageSize}\n";

        return firstLine+secondLine+centerLine+lastLine;

    }

    private String ifAllNull(){
        String firstLine="\n  <if test=\"";
        for(Field fd:fields){
            firstLine+=fd.getName()+" != null || ";
        }
        firstLine=firstLine.substring(0,firstLine.length()-3);
        firstLine+="\" >\n";
        return firstLine;
    }

    public String deleteByPrimaryKey(){
        if(!hasPK) return "\n";

        StringBuilder tableFieldName=new StringBuilder();
        for(int i=0;i<pk.length();i++){
            if(pk.charAt(i)<='Z'&&pk.charAt(i)>='A'){
                tableFieldName.append("_"+pk.substring(i,i+1).toLowerCase());
            }else {
                tableFieldName.append(pk.charAt(i));
            }
        }


        String firstLine="  <delete id=\"deleteByPrimaryKey\" parameterType=\""+className+"\">  \n" +
                "    DELETE FROM "+tableName+" WHERE "+tableFieldName.toString()+" = #{"+pk+"}\n" +
                "  </delete>\n";

        return firstLine;
    }

    public String deleteBy(){

        if(delByFields==null||delByFields.length<1) return "";

        String id="deleteBy";
        for(int i=0;i<delByFields.length-1;i++){
            id+=delByFields[i].substring(0,1).toUpperCase()+delByFields[i].substring(1,delByFields[i].length())+"And";
        }
        String lastfields=delByFields[delByFields.length-1];
        id+=lastfields.substring(0,1).toUpperCase()+lastfields.substring(1,lastfields.length());

        String firstLine="  <delete id=\""+id+"\" parameterType=\""+className+"\">  \n" +
                "    DELETE FROM "+tableName+" WHERE \n";
        String scondLine ="    <trim prefix=\"\" suffix=\"\" suffixOverrides=\"and\" >\n";
        for(String bfd:delByFields){
            String centerLineUnit="        "+toSqlField(bfd)+" = #{"+bfd+"} and\n";
            scondLine+=centerLineUnit;
        }
        scondLine+="    </trim>\n";
        String endLine="  </delete>\n";
        return firstLine+scondLine+endLine;
    }

    public String insertBy(String byField){
        String sqlByField=toSqlField(byField);
        String id="insertBy"+byField.substring(0,1).toUpperCase()+byField.substring(1,byField.length());
        String firstLine="<insert id=\""+id+"\" parameterType=\""+className+"\" >\n" +
                "    insert into "+tableName+"\n";
        String secondLine="    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\n";


        String thirdLine="        select\n" +
                "        <trim prefix=\"\" suffix=\"\" suffixOverrides=\",\" >\n";

        for(Field fd:fields){
            String fieldName=fd.getName();
            String fieldNameSql=toSqlField(fieldName);
            secondLine+="            <if test=\""+fieldName+" != null\" >\n" +
                    "                "+fieldNameSql+",\n" +
                    "            </if>\n";
            thirdLine+="            <if test=\""+fieldName+" != null\" >\n" +
                    "                #{"+fieldName+"},\n" +
                    "            </if>\n";
        }

        secondLine+="    </trim>\n";
        thirdLine+="    </trim>\n";
        String endLine="      from dual\n" +
                "      where not exists (\n" +
                "          select * from "+tableName+"\n" +
                "          where "+sqlByField+" = #{"+byField+"}\n" +
                "      )\n" +
                "  </insert>\n";
        return firstLine+secondLine+thirdLine+endLine;
    }

    /**
     * 会判断fields存不存在，
     * 即使用了where not exists
     * @return
     */
    public String insertByFields(){
        String out="";
        for(String fd: insertByFields){
            out+="\n\n"+insertBy(fd);
        }
        return out;
    }

    public String insertByPrimaryKey(){
        if(!hasPK) return "";
        return insertBy(pk);
    }



    public String toSqlField(String field){
        StringBuilder tableFieldName=new StringBuilder();
        for(int i=0;i<field.length();i++){
            if(field.charAt(i)<='Z'&&field.charAt(i)>='A'){
                tableFieldName.append("_"+field.substring(i,i+1).toLowerCase());
            }else {
                tableFieldName.append(field.charAt(i));
            }
        }
        return tableFieldName.toString();
    }


}
