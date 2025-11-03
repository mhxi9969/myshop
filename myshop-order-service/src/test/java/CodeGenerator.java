import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据实体类和模板，生成controller，service接口，service的impl，实体类VO
 */
public class CodeGenerator {

    public static void main(String[] args) {
        try {
            // 设置模块名
            String basePath = "myshop-order-service/src/main";
            // 设置包名
            String packageBase = "top.mhxi.myshop.order";
            // 设置实体类名，可以多个
            List<String> entities = Arrays.asList("Order","OrderItem");


            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);


            // 设置模板类包的路径
            cfg.setDirectoryForTemplateLoading(new File(basePath + "/resources/templates"));
            cfg.setDefaultEncoding("UTF-8");


            // -------------------- 实体列表 --------------------
            for (String entityName : entities) {
                Map<String, Object> data = new HashMap<>();
                data.put("entity", entityName);
                data.put("entityLower", entityName.substring(0, 1).toLowerCase() + entityName.substring(1));
                data.put("package", packageBase);

                // 生成 Service
                generateFile(cfg, "Service.ftl", data, packageBase + ".service", entityName + "Service.java" , basePath);
                // 生成 ServiceImpl
                generateFile(cfg, "ServiceImpl.ftl", data, packageBase + ".service.impl", entityName + "ServiceImpl.java", basePath);
                // 生成 Controller
               generateFile(cfg, "Controller.ftl", data, packageBase + ".controller", entityName + "Controller.java" ,basePath);
                // 生成 js
                generateFile(cfg, "js.ftl", data, packageBase + ".js", entityName + ".js" ,basePath);
            }

            System.out.println("所有实体代码生成完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateFile(Configuration cfg, String templateName,
                                     Map<String,Object> data, String packageName, String fileName, String basePath) {
        try {
            Template template = cfg.getTemplate(templateName);

            // 根据包名生成目录路径
            String packagePath = basePath + "/java/" + packageName.replace('.', '/');
            File dir = new File(packagePath);
            if (!dir.exists()) {
                boolean created = dir.mkdirs(); // 自动创建多级目录
            }

            File outFile = new File(dir, fileName);
            try (FileWriter writer = new FileWriter(outFile)) {
                template.process(data, writer);
            }
        } catch (IOException | TemplateException e) {
            System.err.println("生成模板失败：" + templateName + " for entity: " + data.get("entity"));
            e.printStackTrace();
        }
    }
}
