package com.lambdaschool.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import com.lambdaschool.bookstore.services.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)

/*****
 * Due to security being in place, we have to switch out WebMvcTest for SpringBootTest
 * @WebMvcTest(value = BookController.class)
 */
@SpringBootTest(classes = BookstoreApplication.class)

/****
 * This is the user and roles we will use to test!
 */
@WithMockUser(username = "admin", roles = {"ADMIN", "DATA"})
public class BookControllerTest
{
    /******
     * WebApplicationContext is needed due to security being in place.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    List<Book> bookList = new ArrayList<>();

    @Before
    public void setUp() throws
            Exception
    {
        /*****
         * The following is needed due to security being in place!
         */
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        /*****
         * Note that since we are only testing bookstore data, you only need to mock up bookstore data.
         * You do NOT need to mock up user data. You can. It is not wrong, just extra work.
         */

        Author a1 = new Author("John", "Mitchell");
        a1.setAuthorid(1);
        Author a2 = new Author("Dan", "Brown");
        a2.setAuthorid(2);
        Author a3 = new Author("Jerry", "Poe");
        a3.setAuthorid(3);
        Author a4 = new Author("Wells", "Teague");
        a4.setAuthorid(4);
        Author a5 = new Author("George", "Gallinger");
        a5.setAuthorid(5);
        Author a6 = new Author("Ian", "Stewart");
        a6.setAuthorid(6);

        Section s1 = new Section("Fiction");
        s1.setSectionid(1);
        Section s2 = new Section("Technology");
        s2.setSectionid(2);
        Section s3 = new Section("Travel");
        s3.setSectionid(3);
        Section s4 = new Section("Business");
        s4.setSectionid(4);
        Section s5 = new Section("Religion");
        s5.setSectionid(5);

        Book b1 = new Book("Flatterland", "9780738206752", 2001, s1);
        b1.getWrotes()
                .add(new Wrote(a6, new Book()));
        b1.setBookid(1);
        bookList.add(b1);

        Book b2 = new Book("Digital Fortess", "9788489367012", 2007, s1);
        b2.getWrotes()
                .add(new Wrote(a2, new Book()));
        b1.setBookid(2);
        bookList.add(b2);

        Book b3 = new Book("The Da Vinci Code", "9780307474278", 2009, s1);
        b3.getWrotes()
                .add(new Wrote(a2, new Book()));
        b1.setBookid(3);
        bookList.add(b3);

        Book b4 = new Book("Essentials of Finance", "1314241651234", 0, s4);
        b4.getWrotes()
                .add(new Wrote(a3, new Book()));
        b4.getWrotes()
                .add(new Wrote(a5, new Book()));
        b1.setBookid(4);
        bookList.add(b4);

        Book b5 = new Book("Calling Texas Home", "1885171382134", 2000, s3);
        b5.getWrotes()
                .add(new Wrote(a4, new Book()));
        b1.setBookid(5);
        bookList.add(b5);
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void listAllBooks() throws
            Exception
    {
        Mockito.when(bookService.findAll()).thenReturn(bookList);
        String apiUrl="/books/books";

        String result = mockMvc.perform(MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        String expected = new ObjectMapper().writeValueAsString(bookList);

        assertEquals(expected, result);
    }

    @Test
    public void getBookById() throws
            Exception
    {
        Mockito.when(bookService.findBookById(1)).thenReturn(bookList.get(0));
        String apiUrl="/books/book/1";

        String result = mockMvc.perform(MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        String expected = new ObjectMapper().writeValueAsString(bookList.get(0));

        assertEquals(expected, result);
    }

    @Test
    public void getNoBookById() throws
            Exception
    {
        Mockito.when(bookService.findBookById(1000)).thenReturn(null);
        String apiUrl="/books/book/1000";

        String result = mockMvc.perform(MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals("", result);
    }

    @Test
    public void addNewBook() throws
            Exception
    {
        Mockito.when(bookService.save(any(Book.class))).thenReturn(bookList.get(1));
        String apiUrl="/books/book/";

        String userJson = new ObjectMapper().writeValueAsString(bookList.get(1));

        String result = mockMvc.perform(MockMvcRequestBuilders.post(apiUrl).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andReturn().getResponse().getContentAsString();

        assertEquals("", result);
    }

    @Test
    public void updateFullBook() throws Exception
    {
        Mockito.when(bookService.save(any(Book.class))).thenReturn(bookList.get(2));
        String apiUrl="/books/book/1";

        String userJson = new ObjectMapper().writeValueAsString(bookList.get(2));

        String result = mockMvc.perform(MockMvcRequestBuilders.put(apiUrl).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andReturn().getResponse().getContentAsString();

        assertEquals("", result);
    }

    @Test
    public void deleteBookById() throws
            Exception
    {
        //Mockito.when(bookService.delete(1)).thenReturn();
        String apiUrl="/books/book/1";

        String result = mockMvc.perform(MockMvcRequestBuilders.delete(apiUrl))
                .andReturn().getResponse().getContentAsString();

        assertEquals("", result);

    }
}