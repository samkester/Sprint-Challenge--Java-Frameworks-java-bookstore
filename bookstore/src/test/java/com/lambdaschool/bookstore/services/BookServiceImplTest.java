package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest
{

    @Autowired
    private BookService bookService;

    @Before
    public void setUp() throws
            Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void a_findAll()
    {
        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void b_findBookById()
    {
        assertEquals("Flatterland", bookService.findBookById(26).getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void c_notFindBookById()
    {
        bookService.findBookById(1000);
    }

    @Test
    public void d_delete()
    {
        bookService.delete(26);
        assertEquals(4, bookService.findAll().size());
    }

    @Test
    public void e_save()
    {
        Author author = new Author();
        author.setAuthorid(20);
        Section section = new Section();
        section.setSectionid(21);

        Book book = new Book("Totally New Book", "1234567890abc", 5, section);
        book.getWrotes().add(new Wrote(author, book));
        bookService.save(book);
        assertEquals(5, bookService.findAll().size());
        assertEquals("Totally New Book", bookService.findBookById(31).getTitle());
    }

    @Test
    public void f_update()
    {
        for(Book b : bookService.findAll())
        {
            System.out.println(b.getTitle() + " " + b.getBookid());
        }
        Author author = new Author();
        author.setAuthorid(20);
        Section section = new Section();
        section.setSectionid(21);

        Book book = new Book("Totally New Book", "1234567890abc", 5, section);
        book.getWrotes().add(new Wrote(author, book));
        book.setBookid(27);

        bookService.update(book, 27);
        for(Book b : bookService.findAll())
        {
            System.out.println(b.getTitle() + " " + b.getBookid());
        }
        assertEquals(5, bookService.findAll().size());
        assertEquals("Totally New Book", bookService.findBookById(27).getTitle());
    }

    @Test
    public void g_deleteAll()
    {
        bookService.deleteAll();
        assertEquals(0, bookService.findAll().size());
    }
}