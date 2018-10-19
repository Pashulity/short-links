package com.pdidkovskiy.shortlinks;

import com.pdidkovskiy.shortlinks.service.LinkService;
import domain.Link;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShortLinksApplicationTests {

    @Autowired
    private LinkService linkService;

    @Before
    public void authorize() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", null,
                AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void concurrentLinkCreationTest() throws InterruptedException {
        String url = "http://www.google.com/ds8783y32h9h3&dshkasasbjksadbnasbdjhdsdbsdfhagdssd";
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IntStream.range(0, 20).forEach(i -> {
            Runnable runnable = createLinkRunnable(url, countDownLatch);
            DelegatingSecurityContextRunnable wrappedRunnable =
                    new DelegatingSecurityContextRunnable(runnable, SecurityContextHolder.getContext());
            new Thread(wrappedRunnable).start();
        });
        countDownLatch.countDown();
        Thread.sleep(3000);

        assertEquals(1, linkService.getAllLinks().stream().
                filter(l -> l.getOriginalLink().equals(url)).count());
    }

    @Test
    public void concurrentHitTest() throws InterruptedException {
        String url = "http://www.google.com/dsasddsa9h3&dshkasasbjksadbnasbdjhdsdbsdfhagdssd";
        Link link = linkService.create(url);
        long numThreads = 20;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IntStream.range(0, (int) numThreads).forEach(i -> {
            Runnable runnable = hitLinkRunnable(link.getShortLink(), countDownLatch);
            DelegatingSecurityContextRunnable wrappedRunnable =
                    new DelegatingSecurityContextRunnable(runnable, SecurityContextHolder.getContext());
            new Thread(wrappedRunnable).start();
        });
        countDownLatch.countDown();
        Thread.sleep(3000);

        assertEquals(numThreads, (long) link.getHits());
    }

    private Runnable createLinkRunnable(String url, CountDownLatch countDownLatch) {
        return () -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            linkService.create(url);
        };
    }

    private Runnable hitLinkRunnable(String url, CountDownLatch countDownLatch) {
        return () -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            linkService.hitLink(url);
        };
    }

}
