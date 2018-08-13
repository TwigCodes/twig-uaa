package com.twigcodes.uaa.web.rest;

import com.twigcodes.uaa.service.ClientService;
import com.twigcodes.uaa.service.dto.ClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientResource {
    private final ClientService clientService;

    @PostMapping
    public ClientDTO add(@RequestBody ClientDTO clientDTO) {
        return clientService.add(clientDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        clientService.delete(id);
    }

    @PutMapping("/{id}")
    public ClientDTO update(@PathVariable String id, @RequestBody ClientDTO clientDTO) {
        return clientService.update(id, clientDTO);
    }

    @GetMapping
    public Page<ClientDTO> findAll(Pageable pageable) {
        return clientService.findAll(pageable);
    }
}
