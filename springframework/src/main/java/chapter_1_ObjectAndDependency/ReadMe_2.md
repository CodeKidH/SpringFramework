## 5. Spring of IOC

#### 5_1. Spring's IOC by using object factory

* ApplicationContext and configural information

  - Bean : Bean Object is made by Spring and IOC has application to this Object
  - Bean factory(IOC Object) : Bean factory(IOC Object) is in charge of creating bean, configured relation in Spring
  - ApplicationContext : If Bean factory  will be extended, We will call it ApplicationContext and It refers to configural information to create bean

* ApplicationContext that use a DaoFactory

        To make a configural information by using DaoFactory

~~~java
