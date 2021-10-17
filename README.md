## **Description of the Bug**
When injecting a bean inside the @EnableGlobalMethodSecurity Configuration class, any method marked with @Cacheable within that bean isn't registered by the Cache abstraction.
Surprisingly simply changing the name of the Cache Configuration bean can simply fix that behavior (e.g: Configuration file names _CacheConfiguration_ + _MethodSecurityConfiguration_ won't work, but _ZCacheConfiguration_ + _MethodSecurityConfiguration_ will work).
**Spring Version**: 2.5.5
**JDK Version**: 11.0.3

## **Steps to reproduce**
- Create a simple Spring Boot project with _Web Security_ dependency
- Enable Cache by creating a configuration bean (**CacheConfiguration.java**)
```
@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport { }
```
- Create a sample Service with a @Cacheable method (**ExampleService.java**)
```
@Service
public class ExampleService {

    @Cacheable(value = "example")
    public UUID getCacheableString(UUID key) {
        return UUID.randomUUID();
    }
} 
```
- Enable GlobalMethodSecurityConfiguration (**MethodSecurityConfiguration .java**) and inject the previous service inside
```
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private final ExampleService service;

    public MethodSecurityConfiguration(@Autowired ExampleService service) {
        this.service = service;
    }
}
```
- Check behavior of Cache Abstraction by enabling TRACE logs in application.properties
```logging.level.org.springframework.cache=TRACE```

### **Logs**
![image](https://user-images.githubusercontent.com/11924004/137640192-b3508328-070d-4088-9f32-b5449989ca5e.png)

## **Expected behavior**
@Cacheable method _getCacheableString_ should be registered in the Cache Abstraction and the Cache should work.

## **Sample solutions**
- Simply rename file _CacheConfiguration_ to _ZCacheConfiguration_, and the @Cacheable method is magically registered.
- Or, remove the injection of _ExampleService_ inside MethodSecurityConfiguration 
### **Logs**
![image](https://user-images.githubusercontent.com/11924004/137640277-b4770566-5af3-4f8b-a162-3d8208ed3b23.png)

## **Sample**
https://github.com/LionelClaudon/issue-with-global-method-security

**Note:**
This may be the same issue that was described here: https://github.com/spring-projects/spring-security/issues/5293
