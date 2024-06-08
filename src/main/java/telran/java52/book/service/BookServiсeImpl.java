package telran.java52.book.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java52.book.dao.AuthorRepository;
import telran.java52.book.dao.BookRepository;
import telran.java52.book.dao.PublisherRepository;
import telran.java52.book.dto.AuthorDto;
import telran.java52.book.dto.BookDto;
import telran.java52.book.dto.exeption.EntityNotFoundExeption;
import telran.java52.book.model.Author;
import telran.java52.book.model.Book;
import telran.java52.book.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiсeImpl implements BookService {
	final BookRepository bookRepository;
	final AuthorRepository authorRepository;
	final PublisherRepository publisherRepository;
	final ModelMapper modelMapper;
	
	@Transactional
	@Override
	public Boolean addBook(BookDto bookDto) {
		if (bookRepository.existsById(bookDto.getIsbn())) {
			return false;
	}
	Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
			.orElse(publisherRepository.save(new Publisher(bookDto.getPublisher())));
	
	Set<Author> authors = bookDto.getAuthors().stream()
			.map(a -> authorRepository.findById(a.getName()).orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
			.collect(Collectors.toSet());
	
	Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
	bookRepository.save(book);
	return true;
	}
	
	@Transactional (readOnly = true)
	@Override
	public BookDto findBookByIsbn(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundExeption::new);
		return modelMapper.map(book, BookDto.class);
	}
	
	@Transactional
	@Override
	public BookDto removeBook(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundExeption::new);
	    bookRepository.delete(book);
	    return modelMapper.map(book, BookDto.class);
	}
	
	@Transactional
	@Override
	public BookDto updateBookTitle(String isbn, String title) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundExeption::new);
	    book.setTitle(title);
	    return modelMapper.map(book, BookDto.class);
	}

	@Transactional (readOnly = true)
	@Override
	public BookDto[] findBooksByAuthor(String authorName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional (readOnly = true)
	@Override
	public BookDto[] findBooksByPublisher(String publisherName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional (readOnly = true)
	@Override
	public AuthorDto[] findBookAuthors(String isbn) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional (readOnly = true)
	@Override
	public Iterable<Publisher> findPublishersByAuthor(String authorName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	@Override
	public AuthorDto removeAuthor(String authorName) {
		// TODO Auto-generated method stub
		return null;
	}

}
