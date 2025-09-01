package ru.serggge.command;

import lombok.RequiredArgsConstructor;
import ru.serggge.dao.UserRepository;

@RequiredArgsConstructor
public class FindAllCommand implements Command {

    private final UserRepository userRepository;

    @Override
    public void execute() {
        userRepository.findAll()
                .forEach(System.out::println);
    }
}