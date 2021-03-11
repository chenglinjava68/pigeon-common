package cn.yiidii.pigeon.common.mail.config;

import cn.yiidii.pigeon.common.mail.core.MailTemplate;
import cn.yiidii.pigeon.common.mail.core.PigeonMailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * 邮件自动装配，在spring的邮件自动配置类MailSenderAutoConfiguration之后
 *
 * @author: YiiDii Wang
 * @create: 2021-03-10 20:50
 */
@Configuration
@RequiredArgsConstructor
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailAutoConfiguration {

    private final MailProperties mailProperties;
    private final JavaMailSender mailSender;
    private final FreeMarkerConfigurer freeMarkerConfigurer;

    @Bean
    public MailTemplate mailTemplate() {
        return new PigeonMailTemplate(mailSender, mailProperties, freeMarkerConfigurer);
    }

}
