﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace RTreeWeb_dotNet.Controllers
{
    public class AnalyticsController : Controller
    {
        [Route("/Analytics")]
        public IActionResult Index()
        {
            return View();
        }
    }
}