# IGSN
IGSN 

Pre-requisite
Java 8
tomcat 8
Maven 3.X

Open the portal as a project in Eclipse
Select File->Import
On the Import window select: Maven -> Existing Maven Projects

Once the import has completed - update/clean/build
right click on project->Maven->update project
Project Clean + build

make sure your eclipse is running on java 8 as well. For more tips and tricks visit 
https://twiki.auscope.org/wiki/Grid/AuscopePortalDevelopmentEnvironmentSetUp26022010

include  src/main/resources/hibernate.cfg.xml for development
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
		"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
		"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
		
<!-- I kept this configuration file for Hibernate-tools in eclipse plugin. However for runtime, persistence.xml should be sufficient -->		
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
        <property name="hibernate.connection.password">*****</property>
        <property name="hibernate.connection.url">jdbc:postgresql://xxx.example.com:8888/xxxx</property>
        <property name="hibernate.connection.username">xxx</property>
        <property name="hibernate.default_schema">xxxx</property>       
    </session-factory>
</hibernate-configuration>
````

include src/main/resources/META-INF/persistence.xml 
```
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
        version="2.0">
    <persistence-unit name="org.hibernate.igsn.jpa" transaction-type="RESOURCE_LOCAL">
       <properties>
           <property name="hibernate.dialect" value="org.hibernate.spatial.dialect.postgis.PostgisDialect"/>

           <property name="hibernate.connection.driver_class" value="org.postgresql.Driver"/>
           <property name="hibernate.connection.url" value="jdbc:postgresql://xxx.example:8888/xxxx"/>
           <property name="hibernate.connection.username" value="xxxx"/>
           <property name="hibernate.connection.password" value="xxxx"/>
           <property name="hibernate.default_schema" value="xxxx"/>   
           <property name="hibernate.connection.pool_size" value="5"/>

           <property name="hibernate.show_sql" value="true"/>
           <property name="hibernate.format_sql" value="true"/>

           <property name="hibernate.max_fetch_depth" value="5"/>

			<!-- setting this to update/validate may be useful in the future which will update db schema based on annotations-->
           <property name="hibernate.hbm2ddl.auto" value="update"/>  
           																

       </properties>
    </persistence-unit>
</persistence>
```
