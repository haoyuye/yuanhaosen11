package com.ssp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ssp.mapper.AddressBookMapper;
import com.ssp.service.AddressBookService;
import com.ssp.entities.AddressBook;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
