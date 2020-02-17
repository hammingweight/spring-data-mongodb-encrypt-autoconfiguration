package com.hammingweight.spring.data.mongodb.encrypt.configuration;

import com.bol.crypt.CryptVault;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(
        classes = { EncryptionConfiguredTest.class },
        properties = { "hammingweight.spring.data.mongodb.encrypt.key=hqHKBLV83LpCqzKpf8OvutbCs+O5wX5BPu3btWpEvXA=" }
)
public class EncryptionConfiguredTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @Before
    public void populateDb() {
        mongoTemplate.dropCollection(Widget.class);
        Widget widget1 = new Widget(1, "Gizmo", 100);
        Widget widget2 = new Widget(2, "Contraption", 120);
        mongoTemplate.save(widget1);
        mongoTemplate.save(widget2);
    }

    @Test
    public void cryptVaultInstantiated() {
        // A CryptVault should have been configured
        assertNotNull(applicationContext.getBean(CryptVault.class));
    }

    @Test
    public void sanityTest() {
        assertEquals(2, mongoTemplate.findAll(Widget.class, "widget").size());
    }

    @Test
    public void testPriceIsNotEncrypted() {
        // We should be able to query on the value of an unencrypted field.
        Query query = new Query();
        query.addCriteria(Criteria.where("price").is(100));
        Collection<Widget> widgets = mongoTemplate.find(query, Widget.class, "widget");
        assertEquals(1, widgets.size());
    }

    @Test
    public void testDescriptionIsEncrypted() {
        // We should not be able to query on the value of an encrypted field.
        Query query = new Query();
        query.addCriteria(Criteria.where("description").is("Contraption"));
        Collection<Widget> widgets = mongoTemplate.find(query, Widget.class, "widget");
        assertEquals(0, widgets.size());
    }
}
